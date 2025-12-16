package com.grig.restapirecipes.dto.response

data class IngredientWithAmountDto(
    val id: Long,
    val name: String,
    val amount: String?,
    val unit: String
)