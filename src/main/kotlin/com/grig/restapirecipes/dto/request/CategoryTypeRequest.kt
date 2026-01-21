package com.grig.restapirecipes.dto.request

import jakarta.validation.constraints.NotBlank

data class CategoryTypeRequest(
    @field:NotBlank val nameType: String
)
