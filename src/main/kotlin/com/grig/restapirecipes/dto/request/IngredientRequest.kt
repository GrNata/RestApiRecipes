package com.grig.restapirecipes.dto.request

import com.fasterxml.jackson.core.sym.Name
import jakarta.validation.constraints.NotBlank

data class IngredientRequest(
    @field:NotBlank val name: String,
    @field:NotBlank val unit: String
)
