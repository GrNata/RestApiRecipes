package com.grig.restapirecipes.dto.request

data class UpdateRecipeIngredientRequest(
    val ingredientId: Long,
    val amount: String
)