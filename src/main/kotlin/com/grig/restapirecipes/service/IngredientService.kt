package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.response.IngredientDto
import org.springframework.stereotype.Service
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.repository.IngredientRepository


@Service
class IngredientService(
    private val ingredientRepository : IngredientRepository
) {

    fun getAll() : List<IngredientDto> =
        ingredientRepository.findAll()
            .map {
                IngredientDto(requireNotNull(it.id), it.name)
            }


    fun getById(id: Long) : IngredientDto =
        ingredientRepository.findById(id)
            .map {
                IngredientDto(requireNotNull(it.id), it.name)
            }
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }
}