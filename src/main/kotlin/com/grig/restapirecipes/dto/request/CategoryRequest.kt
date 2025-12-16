package com.grig.restapirecipes.dto.request

import jakarta.validation.constraints.NotBlank

data class CategoryRequest(
    @field:NotBlank val name: String,
    val image: String?
)
