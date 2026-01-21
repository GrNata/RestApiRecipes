package com.grig.restapirecipes.dto.request

import jakarta.validation.constraints.NotBlank

data class UnitRequest(
    @field:NotBlank val code: String,
    @field:NotBlank val label: String
)