package com.grig.restapirecipes.dto.response

import com.fasterxml.jackson.annotation.JsonProperty

data class OpanFoodFactsResponse(
    val products: List<Product>?
)

data class Product(
    val nutriments: Nutriments?
)

data class Nutriments(
    @JsonProperty("energy-kcal_100g")
    val energyKcal100g: Int?
)
