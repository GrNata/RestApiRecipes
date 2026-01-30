package com.grig.restapirecipes.dto.request

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

data class CreateRecipeRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val description: String,
    val image: String?,
    @field:Min(1) val baseServings: Int,
    @field:NotEmpty val categoryValueIds: List<Long>,       // id выбранных CategoryValue
    @field:NotEmpty val ingredients: List<CreateRecipeIngredientRequest>,
    @field:NotEmpty val steps: List<String>
)
//
//data class CreateRecipeRequest(
//    @field:NotBlank val name: String,
//    @field:NotBlank val description: String,
//    val image: String?,
//    @field:NotEmpty val categoryIds: List<Long>,
//    @field:NotEmpty val ingredients: List<CreateRecipeIngredientRequest>,
//    @field:NotEmpty val steps: List<String>
//)





//  Аннотация   Для чего
//@NotBlank     строка ≠ null и не "   "
//@NotEmpty     список/строка не пустые
//@Size(min=…)  минимальная длина
//@Positive     id > 0

