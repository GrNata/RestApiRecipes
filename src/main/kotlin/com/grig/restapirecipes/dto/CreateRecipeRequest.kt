package com.grig.restapirecipes.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class CreateRecipeRequest(
    val name: String,
    val description: String,
    val image: String?,
    val categoryIds: List<Long>,
    val ingredients: List<CreateRecipeIngredientRequest>,
    val steps: List<String>
)

data class CreateRecipeIngredientRequest(
    val ingredientId: Long,
    val amount: String
)


//data class CreateRecipeRequest(
//    @field:NotBlank val name: String,
//    @field:NotBlank val description: String,
//    val image: String?,
//    @field:NotEmpty val categoryIds: List<Long>,
//    @field:NotEmpty val ingredients: List<IngredientWithAmountDto>,
//    @field:NotEmpty val steps: List<String>
//)

