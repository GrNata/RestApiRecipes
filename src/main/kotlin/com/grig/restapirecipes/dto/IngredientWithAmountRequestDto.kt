package com.grig.restapirecipes.dto

data class IngredientWithAmountRequestDto(
    val ingredientId: Long,
    val amount: String?
)

//	•	unit можно не передавать — берётся из базы.
//	•	name тоже не нужен — берётся из базы через id.
