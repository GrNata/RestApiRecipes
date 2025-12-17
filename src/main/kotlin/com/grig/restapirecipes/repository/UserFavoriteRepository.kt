package com.grig.restapirecipes.repository

import com.grig.restapirecipes.favorite.model.UserFavorite
import com.grig.restapirecipes.favorite.model.UserFavoriteId
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserFavoriteRepository : JpaRepository<UserFavorite, UserFavoriteId> {

    fun existsByUserAndRecipe(user: User, recipe: Recipe) : Boolean

    fun findAllByUser(user: User) : List<UserFavorite>

    fun findByUserAndRecipe(user: User, recipe: Recipe): UserFavorite?

    fun deleteByUserAndRecipe(user: User, recipe: Recipe)
}