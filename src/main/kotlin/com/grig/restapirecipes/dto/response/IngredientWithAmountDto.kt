package com.grig.restapirecipes.dto.response

data class IngredientWithAmountDto(
    val id: Long?,
    val name: String,
    val nameEng: String?,
    val energyKcal100g: Int?,
    val amount: String?,
    val unit: UnitDto
)