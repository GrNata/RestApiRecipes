package com.grig.restapirecipes.dto.request

import com.grig.restapirecipes.model.CategoryType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CategoryValueRequest(
    @field:NotNull val categoryTypeId: Long,
    @field:NotBlank val categoryValue: String
)

data class CategoryValueRequestWithType(
    @field:NotNull val typeId: Long,
    @field:NotBlank val typeName: String,
    @field:NotBlank val categoryValue: String
)
