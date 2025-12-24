package com.grig.restapirecipes.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Positive

data class CreateRecipeIngredientRequest(
    @field:Positive
    val ingredientId: Long,

    @field:NotBlank
    val amount: String,

    @field:NotBlank
    val unitId: Long
)
