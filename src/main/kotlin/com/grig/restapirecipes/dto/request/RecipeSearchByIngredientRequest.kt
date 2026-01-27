package com.grig.restapirecipes.dto.request

data class RecipeSearchByIngredientRequest(
    val ingredientIds: List<Long>
)
