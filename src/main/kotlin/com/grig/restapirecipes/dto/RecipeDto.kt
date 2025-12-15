package com.grig.restapirecipes.dto

data class RecipeDto (
    val id: Long?,
    val name: String,
    val description: String?,
    val image: String?,
    val categories: List<String>,
    val ingredients: List<String>,
    val steps: List<String>
)