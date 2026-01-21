package com.grig.restapirecipes.dto.response

import io.swagger.v3.oas.annotations.media.Schema

data class CategoryTypeDto(
    @Schema(description = "Unique identifier of the categoryType", example = "3")
    val id: Long,
    @Schema(description = "Category nameType", example = "Кухня")
    val nameType: String,
)
