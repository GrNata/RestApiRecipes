package com.grig.restapirecipes.dto.response

data class RecipeDto (
    val id: Long?,
    val name: String,
    val description: String?,
    val image: String?,
    val categories: List<CategoryDto>,
//    val categories: CategoryDto,
    val ingredients: List<IngredientWithAmountDto>,
    val steps: List<String>
)