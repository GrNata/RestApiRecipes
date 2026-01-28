package com.grig.restapirecipes.inregretion.openFoodFacts

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {

//    @Bean
//    fun openFoodFactsWebClient(): WebClient =
//        WebClient.builder()
//            .baseUrl("https://world.openfoodfacts.org")
//            .build()


//    WebClient по умолчанию буферизует максимум 256 KB, а ответ от OpenFoodFacts больше
//    В WebClient нужно увеличить лимит буфера. Пример для твоего клиента:

    @Bean
    fun openFoodGactsWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://world.openfoodfacts.org")
            .codecs { clientCodecConfigurer ->
                clientCodecConfigurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024) //  10 MB
            }
            .build()
    }

}