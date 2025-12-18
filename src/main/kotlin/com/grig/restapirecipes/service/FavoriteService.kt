package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.response.RecipeDto
//import com.grig.restapirecipes.favorite.model.Favorite
import com.grig.restapirecipes.favorite.model.UserFavorite
import com.grig.restapirecipes.favorite.model.UserFavoriteId
import com.grig.restapirecipes.mapper.RecipeMapper
//import com.grig.restapirecipes.repository.FavoriteRepository
import com.grig.restapirecipes.repository.RecipeRepository
import com.grig.restapirecipes.repository.UserFavoriteRepository
import com.grig.restapirecipes.repository.UserRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FavoriteService(
    private val userFavoriteRepository: UserFavoriteRepository,
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository
) {

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun addFavorite(userEmail: String, recipeId: Long) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found: ${userEmail}")

        val recipe = recipeRepository.findById(recipeId)
            .orElseThrow { error("Recipe not found: ${recipeId}") }

        if (userFavoriteRepository.existsByUserAndRecipe(user, recipe)) return
        val favorite = UserFavorite(
            id = UserFavoriteId(user.id!!, recipe.id!!),
            user = user,
            recipe = recipe)
//
        userFavoriteRepository.save(favorite)
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Transactional
    fun removeFavorite(userEmail: String, recipeId: Long) {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found: ${userEmail}")
        val recipe = recipeRepository.findById(recipeId)
            .orElseThrow { IllegalArgumentException("Recipe not found: ${recipeId}") }
        val favorite = userFavoriteRepository.findByUserAndRecipe(user, recipe)
            ?: throw IllegalArgumentException("Favorite not found")
//        userFavoriteRepository.deleteByUserAndRecipe(user, recipe)
        userFavoriteRepository.delete(favorite)
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun listFavoritesByUser(userEmail: String) : List<RecipeDto> {
        val user = userRepository.findByEmail(userEmail)
            ?: throw IllegalArgumentException("User not found: ${userEmail}")

        return userFavoriteRepository.findAllByUser(user).map {
            RecipeMapper.toDto(
                it.recipe,
                it.recipe.recipeIngredients.toList(),
                it.recipe.steps
            )
        }

    }
}