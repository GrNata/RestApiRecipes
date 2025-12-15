package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long> {

    fun findByNameContainingIgnoreCase(name: String): List<Recipe>



    @Query("""
        SELECT DISTINCT r FROM Recipe r
        LEFT JOIN FETCH r.categories
        LEFT JOIN FETCH r.recipeIngredients ri
        LEFT JOIN FETCH ri.ingredient
        LEFT JOIN FETCH r.steps
    """)
    fun findAllWithDetails(): List<Recipe>

    @Query("""
        SELECT DISTINCT r FROM Recipe r
        LEFT JOIN FETCH r.categories
        LEFT JOIN FETCH r.recipeIngredients ri
        LEFT JOIN FETCH ri.ingredient
        LEFT JOIN FETCH r.steps
        WHERE r.id =:id
    """)
    fun findByIdWithDetails(id: Long): Recipe?

    @Query("""
        SELECT r FROM Recipe r
        LEFT JOIN FETCH r.categories
        WHERE r.id = :id
    """)
    fun findRecipeBase(id: Long): Recipe?
}