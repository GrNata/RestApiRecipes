package com.grig.restapirecipes.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class CreateRecipeRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val description: String,
    val image: String?,
    @field:NotEmpty val categoryIds: List<Long>,
    @field:NotEmpty val ingredients: List<CreateRecipeIngredientRequest>,
    @field:NotEmpty val steps: List<String>
)

data class CreateRecipeIngredientRequest(
    @field:Positive
    val ingredientId: Long,

    @field:NotBlank
    val amount: String
)



//  Аннотация   Для чего
//@NotBlank     строка ≠ null и не "   "
//@NotEmpty     список/строка не пустые
//@Size(min=…)  минимальная длина
//@Positive     id > 0

