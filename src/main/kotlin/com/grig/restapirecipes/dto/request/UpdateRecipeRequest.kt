package com.grig.restapirecipes.dto.request

data class UpdateRecipeRequest(
    val name: String?,
    val description: String?,
    val image: String?,
    val categoryIds: List<Long>?,
    val ingredients: List<UpdateRecipeIngredientRequest>?,
    val steps: List<String>?
)


