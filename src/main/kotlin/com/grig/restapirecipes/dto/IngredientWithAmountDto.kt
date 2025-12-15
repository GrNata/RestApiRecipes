package com.grig.restapirecipes.dto

data class IngredientWithAmountDto(
    val id: Long,
    val name: String,
    val amount: String?,
    val unit: String
)
