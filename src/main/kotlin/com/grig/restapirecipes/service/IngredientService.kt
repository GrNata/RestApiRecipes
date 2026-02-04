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
                IngredientDto(requireNotNull(it.id), it.name, it.nameEng, it.energyKcal100g)
            }


    fun getByIdIngredient(id: Long) : IngredientDto =
        ingredientRepository.findById(id)
            .map {
                IngredientDto(requireNotNull(it.id), it.name, it.nameEng, it.energyKcal100g)
            }
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }


    @PreAuthorize("hasRole('ADMIN')")
    fun createIngredient(request: IngredientRequest) : Ingredient {
        val ingredient = Ingredient(request.name, request.nameEng, request.energyKcal100g)
        return ingredientRepository.save(ingredient)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateIngredient(id: Long, request: IngredientRequest) : Ingredient {

        println("ADMIN: IngredientService: id=$id,  request: $request")

        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }

        println("ADMIN: IngredientService: findById,  ingredient: $ingredient")

        ingredient.name = request.name
        ingredient.nameEng = request.nameEng
        ingredient.energyKcal100g = request.energyKcal100g

        val response = ingredientRepository.save(ingredient)

        println("ADMIN: IngredientService: response: $response")
        return response
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteIngredient(id: Long) {
        val ingredient = ingredientRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Ingredient with id ${id} not found.") }
        ingredientRepository.delete(ingredient)
    }

//    Это будет fallback, если OFF не ответил.
    fun findByNameEngIgnoreCase(nameEng: String) =
        ingredientRepository.findByNameEngIgnoreCase(nameEng)
}