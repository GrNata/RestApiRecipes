package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.Ingredient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : JpaRepository<Ingredient, Long> {
    fun findByName(name: String): Ingredient?
}