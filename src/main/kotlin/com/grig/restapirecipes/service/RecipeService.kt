package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CreateRecipeRequest
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.CookingStep
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.model.RecipeIngredient
import com.grig.restapirecipes.repository.CategoryRepository
import com.grig.restapirecipes.repository.IngredientRepository
import com.grig.restapirecipes.repository.RecipeIngredientRepository
import com.grig.restapirecipes.repository.RecipeRepository
import com.grig.restapirecipes.repository.StepRepository
import com.grig.restapirecipes.repository.UnitRepository
import com.grig.restapirecipes.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeIngredientRepository: RecipeIngredientRepository,
    private val stepRepository: StepRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository,
    private val userRepository: UserRepository,
    private val unitRepository: UnitRepository
) {

    @Transactional(readOnly = true)
    fun getAllRecipes() : List<RecipeDto>  =
        recipeRepository.findAll().map { recipe ->
//            УБРАТЬ !!
            val ingredients = recipeIngredientRepository.findIngredients(recipe.id!!)
            val steps = stepRepository.findSteps(recipe.id!!)
            RecipeMapper.toDto(recipe, ingredients, steps)
        }

    fun getRecipeById(id: Long): RecipeDto {
        val recipe = recipeRepository.findRecipeBase(id)
//            ?: throw RuntimeException("Recipe id = ${id} not found")
            ?: throw RecipeNotFoundException("Recipe id = ${id} not found")
        val ingredients = recipeIngredientRepository.findIngredients(id)
        val steps = stepRepository.findSteps(id)

        return RecipeMapper.toDto(recipe, ingredients, steps)
    }

// комбинационный поиск или название, или ингредиент, или все вместе
    @Transactional(readOnly = true)
    fun search(name: String?, ingredient: String?) : List<RecipeDto> {
        var spec: Specification<Recipe>? = null

        if (!name.isNullOrBlank()) {
            spec = RecipeSpecification.hasName(name)
        }
        if (!ingredient.isNullOrBlank()) {
            spec = if (spec != null) spec.and(RecipeSpecification.hasIngredient(ingredient))
                   else RecipeSpecification.hasIngredient(ingredient)
        }
        val recipes = if (spec != null) recipeRepository.findAll(spec)
                      else recipeRepository.findAll()
        return recipes.map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(requireNotNull(recipe.id))
            val steps = stepRepository.findSteps(requireNotNull(recipe.id))
            RecipeMapper.toDto(recipe, ingredients, steps)
        }
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
//    fun createRecipe(request: CreateRecipeRequest) : Recipe {
    fun createRecipe(userEmail: String, request: CreateRecipeRequest) : Recipe {
        val user = userRepository.findByEmailWithRoles(userEmail)
            ?: throw IllegalArgumentException("User not found: ${userEmail}")
//            ?: throw AuthenticationCredentialsNotFoundException("User not found: ${userEmail}")

        val categories = categoryRepository.findAllById(request.categoryIds).toMutableSet()

        val recipe = Recipe(
            name = request.name,
            description = request.description,
            image = request.image,
            categories = categories
        )
        recipeRepository.save(recipe)

        request.ingredients.forEach { dto ->
            val ingredient = ingredientRepository.findById(dto.ingredientId)
//                .orElseThrow { IllegalArgumentException("Ingredient not found: ${dto.ingredientId}") }
                .orElseThrow { RecipeNotFoundException("Ingredient not found: ${dto.ingredientId}") }
            val unitEntity = unitRepository.findById(dto.unitId)
                .orElseThrow { RecipeNotFoundException("Unit not found: ${dto.unitId}") }

            recipe.recipeIngredients.add(
                RecipeIngredient(
                    recipe = recipe,
                    ingredient = ingredient,
                    amount = dto.amount,
                    unit = unitEntity
                )
            )
        }
         request.steps.forEachIndexed { index, step ->
             recipe.steps.add(
                 CookingStep(
                     recipe = recipe,
                     stepNumber = index + 1,
                     description = step
                 )
             )
         }
        return recipeRepository.save(recipe)
    }

    // Редактирование — только автор или ADMIN
    @PreAuthorize("hasRole('ADMIN') or @recipeSecurity.isAuthor(#id, authentication.name)")
    @Transactional
    fun updateRecipe(id: Long, request: UpdateRecipeRequest) : Recipe {
        val recipe = recipeRepository.findById(id)
//            .orElseThrow { RuntimeException("Recipe is not found") }
            .orElseThrow { RecipeNotFoundException("Recipe is not found") }

        request.name?.let { recipe.name = it }
        request.description?.let { recipe.description = it }
        request.image?.let { recipe.image = it }

        request.categoryIds?.let {
            recipe.categories.clear()
            recipe.categories.addAll(categoryRepository.findAllById(it))
        }

        request.ingredients?.let {
            recipe.recipeIngredients.clear()
            it.forEach { dto ->
                val ingredient = ingredientRepository.findById(dto.ingredientId)
//                    .orElseThrow { RuntimeException("Ingredient not found: ${dto.ingredientId}") }
                    .orElseThrow { RecipeNotFoundException("Ingredient not found: ${dto.ingredientId}") }
                val unitEntity = unitRepository.findById(dto.unitId)
                    .orElseThrow { RecipeNotFoundException("Unit not found: ${dto.unitId}") }

                recipe.recipeIngredients.add(RecipeIngredient(recipe, ingredient, dto.amount, unitEntity))
            }
        }

        request.steps?.let {
            recipe.steps.clear()
            it.forEachIndexed { index, step ->
                recipe.steps.add(CookingStep(recipe, index + 1, step))
            }
        }

            return recipeRepository.save(recipe)
        }

    @PreAuthorize("hasRole('ADMIN') or @recipeSecurity.isAuthor(#id, authentication.name)")
    @Transactional
    fun deleteRecipe(id: Long) {
        val recipe = recipeRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Recipe with id $id not found") }

        recipeRepository.delete(recipe)
    }

    //    Pagination + Sort
    @Transactional(readOnly = true)
    fun searchRecipes(
        name: String?,
        ingredient: String?,
        page: Int = 0,
        size: Int = 0,
        sortBy: String = "name",
        direction: String = "ASC"
    ) : Page<RecipeDto> {

        val pageable = PageRequest.of(
            page,
            size,
            if (direction.uppercase() == "ASC") Sort.by(sortBy).ascending()
                  else Sort.by(sortBy).descending()
        )

        val combinedSpec: Specification<Recipe>? = listOfNotNull(
            name?.takeIf { it.isNotBlank() }?.let { RecipeSpecification.hasName(it) },
            ingredient?.takeIf { it.isNotBlank() }?.let { RecipeSpecification.hasIngredient(it) }
        ).reduceOrNull { acc, s -> acc.and(s) }

//        val recipesPage: Page<Recipe> = recipeRepository.findAll(combinedSpec as Specification<Recipe>, pageable)
        val recipesPage = if (combinedSpec != null) {
            recipeRepository.findAll(combinedSpec, pageable)
        } else {
            recipeRepository.findAll(pageable)
        }

        return recipesPage.map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(requireNotNull(recipe.id))
            val staps = stepRepository.findSteps(requireNotNull(recipe.id))
            RecipeMapper.toDto(recipe, ingredients, staps)
        }
    }

}