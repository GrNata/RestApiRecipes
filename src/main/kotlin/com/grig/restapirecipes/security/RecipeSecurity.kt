package com.grig.restapirecipes.security

import com.grig.restapirecipes.repository.RecipeRepository
import com.grig.restapirecipes.repository.UserRepository
import org.springframework.stereotype.Component


//  Кастомный бин проверки авторства рецепта (опционально)
@Component
class RecipeSecurity(
    private val recipeRepository: RecipeRepository,
    private val userRepository: UserRepository
) {

    fun isAuthor(recipeId: Long, userEmail: String) : Boolean {
        val recipe = recipeRepository.findById(recipeId)
            .orElse(null) ?: return false
        val user = userRepository.findByEmail(userEmail) ?: return false
        return recipe.createBy?.id == user.id
    }
}