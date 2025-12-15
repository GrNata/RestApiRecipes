package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.RecipeIngredient
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeIngredientRepository : JpaRepository<RecipeIngredient, Long> {

    @Query("""
        SELECT ri FROM RecipeIngredient ri
        JOIN FETCH ri.ingredient
        WHERE ri.recipe.id = :recipeId
    """)
    fun findIngredients(recipeId: Long) : List<RecipeIngredient>
}