package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.RecipeDto
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.repository.RecipeIngredientRepository
import com.grig.restapirecipes.repository.RecipeRepository
import com.grig.restapirecipes.repository.StepRepository
import org.springframework.stereotype.Service

@Service
class RecipeService(
    private val recipeRepository: RecipeRepository,
    private val recipeIngredientRepository: RecipeIngredientRepository,
    private val stepRepository: StepRepository
    ) {

    fun getAllRecipes() : List<RecipeDto>  =
        recipeRepository.findAll().map { recipe ->
            val ingredients = recipeIngredientRepository.findIngredients(recipe.id!!)
            val steps = stepRepository.findSteps(recipe.id!!)
            RecipeMapper.toDto(recipe, ingredients, steps)
        }

    fun getRecipeById(id: Long): RecipeDto {
        val recipe = recipeRepository.findRecipeBase(id)
            ?: throw RuntimeException("Recipe id = ${id} not found")
        val ingredients = recipeIngredientRepository.findIngredients(id)
        val steps = stepRepository.findSteps(id)

        return RecipeMapper.toDto(recipe, ingredients, steps)
    }

}