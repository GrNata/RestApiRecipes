package com.grig.restapirecipes.dto.request

//	•	unit можно не передавать — берётся из базы.
//	•	name тоже не нужен — берётся из базы через id.

data class IngredientWithAmountRequestDto(
    val ingredientId: Long,
    val amount: String?
)