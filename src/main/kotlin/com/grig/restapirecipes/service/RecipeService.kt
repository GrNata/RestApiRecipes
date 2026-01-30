package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CreateRecipeRequest
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
import com.grig.restapirecipes.dto.response.IngredientWithAmountDto
import com.grig.restapirecipes.dto.response.UnitDto
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.mapper.UnitMapper
import com.grig.restapirecipes.model.CookingStep
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.model.RecipeIngredient
import com.grig.restapirecipes.repository.CategoryValueRepository
import com.grig.restapirecipes.repository.IngredientRepository
import com.grig.restapirecipes.repository.RecipeIngredientRepository
import com.grig.restapirecipes.repository.RecipeRepository
import com.grig.restapirecipes.repository.StepRepository
import com.grig.restapirecipes.repository.UnitRepository
import com.grig.restapirecipes.repository.UserRepository
import com.zaxxer.hikari.HikariDataSource
import org.springframework.data.domain.Page
import org.springframework.data.domain.Sort
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.IllegalArgumentException

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeIngredientRepository: RecipeIngredientRepository,
    private val stepRepository: StepRepository,
//    private val categoryRepository: CategoryRepository,
    private val categoryValueRepository: CategoryValueRepository,
    private val ingredientRepository: IngredientRepository,
    private val userRepository: UserRepository,
    private val unitRepository: UnitRepository,
    private val nutritionService: NutritionService
) {

    @Transactional(readOnly = true)
    fun getMyRecipes(userEmail: String, pageable: Pageable) : Page<RecipeDto> {
        val recipesPage: Page<Recipe> = recipeRepository.findAllByCreateByEmail(userEmail, pageable)

        return recipesPage.map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(requireNotNull(recipe.id))
            val steps = stepRepository.findSteps(requireNotNull(recipe.id))
            RecipeMapper.toDto(recipe, ingredients, steps, null)
        }
    }

    @Transactional(readOnly = true)
    fun getAllRecipes() : List<RecipeDto>  =
        recipeRepository.findAll().map { recipe ->
//            УБРАТЬ !!
            val ingredients = recipeIngredientRepository.findIngredients(recipe.id!!)
            val steps = stepRepository.findSteps(recipe.id!!)
            RecipeMapper.toDto(recipe, ingredients, steps, null)
        }

    fun getRecipeById(id: Long): RecipeDto {
        val recipe = recipeRepository.findRecipeBase(id)
//            ?: throw RuntimeException("Recipe id = ${id} not found")
            ?: throw RecipeNotFoundException("Recipe id = ${id} not found")
        val ingredients = recipeIngredientRepository.findIngredients(id)
        val steps = stepRepository.findSteps(id)

        println("Kcal_100: RecipeService: START calculeteTotalCalories")

//        Вычисляем totalCalories
        val totalCalories = calculeteTotalCalories(ingredients.toListIngredientsWithAmountDto())

//        println("Kcal_100: RecipeService: calculeteTotalCalories, energyKcal100g = ${ingredients.forEach { it.ingredient.energyKcal100g }}")
        println("Kcal_100: RecipeService: calculeteTotalCalories, totalCalories = ${totalCalories}")

        return RecipeMapper.toDto(recipe, ingredients, steps, totalCalories)
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
            RecipeMapper.toDto(recipe, ingredients, steps, null)
        }
    }


    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
//    fun createRecipe(request: CreateRecipeRequest) : Recipe {
    fun createRecipe(userEmail: String, request: CreateRecipeRequest) : Recipe {
        val user = userRepository.findByEmailWithRoles(userEmail)
            ?: throw IllegalArgumentException("User not found: ${userEmail}")
//            ?: throw AuthenticationCredentialsNotFoundException("User not found: ${userEmail}")
        println("RECIPE: SERVICE Создание нового рецепта user: ${user}")
        val categoryValues = categoryValueRepository.findAllById(request.categoryValueIds).toMutableSet()
//        val categories = categoryRepository.findAllById(request.categoryIds).toMutableSet()
        println("RECIPE: SERVICE Создание нового рецепта categoryValues: ${categoryValues}")
//        Проверку: 1 значение на 1 тип (Это защитит бизнес-логику)
        val dublicatedTypes = categoryValues.groupBy { it.categoryType.id }.filter { it.value.size > 1 }
        if (dublicatedTypes.isNotEmpty()) {
            throw IllegalArgumentException("Only one category per type is allowed")
        }
        println("RECIPE: SERVICE Создание нового рецепта dublicatedTypes: ${dublicatedTypes}")

        val recipe = Recipe(
            name = request.name,
            description = request.description,
            image = request.image,
            baseServings = request.baseServings,
            categories = categoryValues,
//            categories = categories,
            createBy = user
        )
        println("RECIPE: SERVICE Создание нового рецепта recipe: ${recipe}")
        recipeRepository.save(recipe)
        println("RECIPE: SERVICE Создание нового рецепта AFTER SAVE recipe: ${recipe}")

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
        println("RECIPE: SERVICE Создание нового рецепта INGREDIENT recipe: ${recipe}")
         request.steps.forEachIndexed { index, step ->
             recipe.steps.add(
                 CookingStep(
                     recipe = recipe,
                     stepNumber = index + 1,
                     description = step
                 )
             )
         }
        println("RECIPE: SERVICE Создание нового рецепта STEPS recipe: ${recipe}")

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
        request.baseServings?.let { recipe.baseServings = it }

        request.categoryIds?.let {
            recipe.categories.clear()
            recipe.categories.addAll(categoryValueRepository.findAllById(it))
//            recipe.categories.addAll(categoryRepository.findAllById(it))
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

        println("CATEGORY-ch RecipeService: recipes: ${recipesPage.toList()}")

        return recipesPage.map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(requireNotNull(recipe.id))
            val staps = stepRepository.findSteps(requireNotNull(recipe.id))
            RecipeMapper.toDto(recipe, ingredients, staps, null)
        }
    }

    //    Поис рецептов по ингредиентам
    @Transactional(readOnly = true)
    fun searchByIngredients(ingredientIds: List<Long>): List<RecipeDto> {
        val recipes = recipeRepository.findAllIngredients(
            ingredientIds = ingredientIds,
            count = ingredientIds.size.toLong()
        )
        return recipes.map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(requireNotNull(recipe.id))
            val steps = stepRepository.findSteps(requireNotNull(recipe.id))
            RecipeMapper.toDto(recipe, ingredients, steps, null)
        }
    }

    fun List<RecipeIngredient>.toListIngredientsWithAmountDto(): List<IngredientWithAmountDto> {
        return this.map {
            IngredientWithAmountDto(
                it.ingredient.id,
                it.ingredient.name,
                it.ingredient.nameEng,
                it.ingredient.energyKcal100g,
                it.amount,
                UnitDto(it.unit.id, it.unit.code, it.unit.label)
            )
        }
    }

//    // 🔹 Вычисляем totalCalories
    fun calculeteTotalCalories(ingredientsWithAmountDto: List<IngredientWithAmountDto>) =

        ingredientsWithAmountDto.sumOf { ingredient ->
            val amount = ingredient.amount?.toDoubleOrNull() ?: 0.0


            // берём калории напрямую
            val calories = nutritionService.getCaloriesForIngredientWithAmountDto(ingredient) ?: 0

            calories

//            val kcal100 =
//                ingredient.energyKcal100g ?: nutritionService.getCaloriesForIngredientWithAmountDto(ingredient)
//            ingredient.energyKcal100g?.let { kcal100 ->
//                nutritionService.calculateCaloriesForIngredient(
//                    iningredientDto = ingredient,
//                    amount = amount,
//                    unit = ingredient.unit
//                ) ?: 0
//            } ?: 0

        }

}