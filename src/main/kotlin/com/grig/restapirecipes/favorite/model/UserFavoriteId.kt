package com.grig.restapirecipes.favorite.model

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
data class UserFavoriteId(
    @Column(name = "user_id")
    val userId: Long = 0,

    @Column(name = "recipe_id")
    val recipeId: Long = 0

) : Serializable
