package com.grig.restapirecipes.dto

import io.swagger.v3.oas.annotations.media.Schema


data class CategoryDto(
//    @Schema(description = "...") можно добавлять к полям DTO
    @Schema(description = "Unique identifier of the category", example = "3")
    val id: Long,
    @Schema(description = "Category name", example = "Дессерт")
    val name: String,
    @Schema(description = "Image URL for category", example = "http://example.com/image.png")
    val image: String?
)
