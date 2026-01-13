package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.security.JwtTokenProvider
import com.grig.restapirecipes.service.FavoriteService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.slf4j.Logger
import org.slf4j.LoggerFactory



// FavoritesController (JWT защищенные методы)
@RestController
@RequestMapping("api/favorites")
@Tag(name = "Favorites", description = "Методы для работы с избранными")
class FavoriteController(
    private val favoriteService: FavoriteService,
    private val jwtTokenProvider: JwtTokenProvider
) {

//    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val logger: Logger = LoggerFactory.getLogger(FavoriteController::class.java)

    private fun getCurrentUserEmail() : String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.name
            ?: throw IllegalArgumentException("User name not found: ${authentication?.name}")
    }

    @Operation(
        summary = "Добавить рецепт в избранное",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @PostMapping("/{recipeId}")
    fun addFavorite(@PathVariable recipeId: Long) : ResponseEntity<Unit> {
        val userEmail = getCurrentUserEmail()
        favoriteService.addFavorite(userEmail, recipeId)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(
        summary = "Удалить рецепт из избранного",
        security = [SecurityRequirement(name = "bearerAuth")]
    )
    @DeleteMapping("/{recipeId}")
    fun removeFavorite(@PathVariable recipeId: Long) : ResponseEntity<Unit> {
        val userEmail = getCurrentUserEmail()
        favoriteService.removeFavorite(userEmail, recipeId)
        return ResponseEntity.noContent().build()
    }

    @Operation(
        summary = "Получить избранные рецепты пользователя",
        security = [SecurityRequirement(name = "bearerAuth")]
    )

    @GetMapping
    fun listFavorite(): ResponseEntity<List<RecipeDto>> {
        println("Метод listFavorite() вызван")
        logger.info("метод listFavorite()")
        val authentication  = SecurityContextHolder.getContext().authentication
        if (authentication == null || !authentication.isAuthenticated) {
            // Незалогиненный пользователь — безопасно возвращаем пустой список
            logger.info("Незалогиненный пользователь")
            return ResponseEntity.ok(emptyList())
        }
        val userEmail = authentication.name

        logger.info("Залогиненный пользователь userEmail: $userEmail")

        val favorites = favoriteService.listFavoritesByUser(userEmail)
        return ResponseEntity.ok(favorites)
    }

//    @GetMapping
//    fun listFavorite() : ResponseEntity<List<RecipeDto>> {
//        val userEmail = getCurrentUserEmail()
//        val favorites = favoriteService.listFavoritesByUser(userEmail)
//        return ResponseEntity.ok(favorites)
//    }

}