package com.grig.restapirecipes.dto.response

data class RecipeDto (
    val id: Long,
    val name: String,
    val description: String?,
    val image: String?,
    val baseServings: Int,
    val categoryValues: Map<String, CategoryValueDto>,
    val ingredients: List<IngredientWithAmountDto>,
    val steps: List<String>,

    val totalCalories: Int?
)