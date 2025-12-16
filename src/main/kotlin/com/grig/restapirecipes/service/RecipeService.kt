package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.CreateRecipeRequest
import com.grig.restapirecipes.dto.RecipeDto
import com.grig.restapirecipes.dto.UpdateRecipeRequest
import com.grig.restapirecipes.exception.NotFoundException
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.CookingStep
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.model.RecipeIngredient
import com.grig.restapirecipes.model.RecipeIngredientId
import com.grig.restapirecipes.repository.CategoryRepository
import com.grig.restapirecipes.repository.IngredientRepository
import com.grig.restapirecipes.repository.RecipeIngredientRepository
import com.grig.restapirecipes.repository.RecipeRepository
import com.grig.restapirecipes.repository.StepRepository
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.PutMapping

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeIngredientRepository: RecipeIngredientRepository,
    private val stepRepository: StepRepository,
    private val categoryRepository: CategoryRepository,
    private val ingredientRepository: IngredientRepository
) {

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
            ?: throw NotFoundException("Recipe id = ${id} not found")
        val ingredients = recipeIngredientRepository.findIngredients(id)
        val steps = stepRepository.findSteps(id)

        return RecipeMapper.toDto(recipe, ingredients, steps)
    }

    fun searchRecipe(
        name: String?,
        ingredient: String?
    ) : List<RecipeDto> {
        val recipes = when {
            !name.isNullOrBlank() ->
                recipeRepository.searchByName(name)
            !ingredient.isNullOrBlank() ->
                recipeRepository.searchByIngredient(ingredient)

            else ->
                recipeRepository.findAll()
        }
        return recipes.map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(requireNotNull(recipe.id))
            val steps = stepRepository.findSteps(requireNotNull(recipe.id))
            RecipeMapper.toDto(recipe, ingredients, steps)
        }
    }

    @Transactional
    fun createRecipe(request: CreateRecipeRequest) : Recipe {
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
                .orElseThrow { NotFoundException("Ingredient not found: ${dto.ingredientId}") }
            recipe.recipeIngredients.add(
                RecipeIngredient(
                    recipe = recipe,
                    ingredient = ingredient,
                    amount = dto.amount
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

    @Transactional
    fun updateRecipe(id: Long, request: UpdateRecipeRequest) : Recipe {
        val recipe = recipeRepository.findById(id)
//            .orElseThrow { RuntimeException("Recipe is not found") }
            .orElseThrow { NotFoundException("Recipe is not found") }

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
                    .orElseThrow { NotFoundException("Ingredient not found: ${dto.ingredientId}") }
                recipe.recipeIngredients.add(RecipeIngredient(recipe, ingredient, dto.amount))
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


}