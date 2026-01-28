package com.grig.restapirecipes.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class IngredientDto(
    @Schema(description = "Unique indentifier of the ingredient", example = "1")
    val id: Long,
    @Schema(description = "Ingredient name", example = "Картофель")
    val name: String,
    @Schema(description = "Ingredient name english", example = "Potato")
    val nameEnglish: String?,
    @Schema(description = "Ingredient energy Kcal in 100g", example = "100")
    val energyKcal100g: Int?
)
