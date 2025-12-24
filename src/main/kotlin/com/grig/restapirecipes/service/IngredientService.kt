package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.IngredientRequest
import com.grig.restapirecipes.dto.response.IngredientDto
import org.springframework.stereotype.Service
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.Ingredient
import com.grig.restapirecipes.repository.IngredientRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.transaction.annotation.Transactional


@Service
class IngredientService(
    private val ingredientRepository : IngredientRepository
) {

    @Transactional(readOnly = true)
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


    @PreAuthorize("hasRole('ADMIN')")
    fun createIngredient(request: IngredientRequest) : Ingredient {
//        val ingredient = Ingredient(request.name, request.unit)
        val ingredient = Ingredient(request.name)
        return ingredientRepository.save(ingredient)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateIngredient(id: Long, request: IngredientRequest) : Ingredient {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }
        ingredient.name = request.name
//        ingredient.unit = request.unit
        return ingredientRepository.save(ingredient)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteIngredient(id: Long) {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }
        ingredientRepository.delete(ingredient)
    }
}