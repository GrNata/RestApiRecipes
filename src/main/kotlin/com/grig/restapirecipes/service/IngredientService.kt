package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.IngredientRequest
import com.grig.restapirecipes.dto.response.IngredientDto
import org.springframework.stereotype.Service
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.Ingredient
import com.grig.restapirecipes.repository.IngredientRepository


@Service
class IngredientService(
    private val ingredientRepository : IngredientRepository
) {

    fun getAllingredients() : List<IngredientDto> =
        ingredientRepository.findAll()
            .map {
                IngredientDto(requireNotNull(it.id), it.name)
            }


    fun getByIdIngredient(id: Long) : IngredientDto =
        ingredientRepository.findById(id)
            .map {
                IngredientDto(requireNotNull(it.id), it.name)
            }
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }


    fun createIngredient(request: IngredientRequest) : Ingredient {
        val ingredient = Ingredient(request.name, request.unit)
        return ingredientRepository.save(ingredient)
    }

    fun updateIngredient(id: Long, request: IngredientRequest) : Ingredient {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }
        ingredient.name = request.name
        ingredient.unit = request.unit
        return ingredientRepository.save(ingredient)
    }

    fun deleteIngredient(id: Long) {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }
        ingredientRepository.delete(ingredient)
    }
}