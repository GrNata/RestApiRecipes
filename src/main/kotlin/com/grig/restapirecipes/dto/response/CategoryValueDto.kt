package com.grig.restapirecipes.dto.response

import com.grig.restapirecipes.model.CategoryType
import io.swagger.v3.oas.annotations.media.Schema

data class CategoryValueDto(
    @Schema(description = "Unique identifier of the category", example = "3")
    val id: Long,
    @Schema(description = "Category categoryType", example = "ссылка на тип категории - id")
    val typeId: Long,
    @Schema(description = "Category categoryType", example = "ссылка на тип категории - название типа")
    val typeName: String,
    @Schema(description = "Image URL for category", example = "салат\", \"итальянская")
    val categoryValue: String
)
