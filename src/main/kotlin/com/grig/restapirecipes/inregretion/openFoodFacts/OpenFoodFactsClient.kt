package com.grig.restapirecipes.inregretion.openFoodFacts

import com.grig.restapirecipes.dto.response.OpanFoodFactsResponse
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import kotlin.jvm.java
import kotlin.math.roundToInt

@Component
class OpenFoodFactsClient(
    private val webClient: WebClient
) {

    fun getCaloriesPer100g(query: String): Int? {
        val response = webClient.get()
            .uri {
                it.path("/cgi/search.pl")
                    .queryParam("search_terms", query)
                    .queryParam("search_simple", 1)
                    .queryParam("action", "process")
                    .queryParam("json", 1)
                    .build()
            }
            .retrieve()
            .bodyToMono(OpanFoodFactsResponse::class.java)
            .block()

        println("OFF response: ${response?.products?.firstOrNull()?.nutriments}")

        // без округдением
        return response?.products
            ?.firstOrNull()
            ?.nutriments
            ?.energyKcal100g    //      уже Int, можно возвращать без roundToInt()
//// с округдением
//        return response?.products
//            ?.firstOrNull()
//            ?.nutriments
//            ?.energyKcal100g
//            ?.toDouble()
//            ?.roundToInt()
    }
}