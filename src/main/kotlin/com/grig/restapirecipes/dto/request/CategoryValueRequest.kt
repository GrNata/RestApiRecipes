package com.grig.restapirecipes.dto.request

import com.grig.restapirecipes.model.CategoryType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CategoryValueRequest(
    @field:NotNull val categoryTypeId: Long,
    @field:NotBlank val categoryValue: String
)
