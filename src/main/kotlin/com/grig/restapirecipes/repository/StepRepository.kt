package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.CookingStep
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface StepRepository : JpaRepository<CookingStep, Long> {

    @Query("""
        SELECT s FROM CookingStep s
        WHERE s.recipe.id = :recipeId
        ORDER BY s.stepNumber
    """)
    fun findSteps(recipeId: Long) : List<CookingStep>
}