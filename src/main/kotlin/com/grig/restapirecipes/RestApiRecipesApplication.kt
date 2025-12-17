package com.grig.restapirecipes

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

//Swagger UI: http://localhost:8080/swagger-ui/index.html

@SpringBootApplication
class RestApiRecipesApplication

fun main(args: Array<String>) {
    runApplication<RestApiRecipesApplication>(*args)
}


//      Swagger:
//  http://localhost:8080/swagger-ui/index.html#/recipe-controller/updateRecipe