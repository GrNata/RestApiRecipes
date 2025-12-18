//package com.grig.restapirecipes.repository
//
//import com.grig.restapirecipes.favorite.model.Favorite
//import com.grig.restapirecipes.favorite.model.FavoriteId
//import com.grig.restapirecipes.model.Recipe
//import com.grig.restapirecipes.user.model.User
//import org.springframework.data.jpa.repository.JpaRepository
//import org.springframework.stereotype.Repository
//
//@Repository
//interface FavoriteRepository : JpaRepository<Favorite, FavoriteId> {
//
//    fun findAllByUser(user: User) : List<Favorite>
//
//    fun findByUserAndRecipe(user: User, recipe: Recipe) : Favorite
//
//    fun existsByUserAndRecipe(user: User, recipe: Recipe) : Boolean
//}