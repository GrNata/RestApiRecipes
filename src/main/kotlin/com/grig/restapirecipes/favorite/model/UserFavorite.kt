package com.grig.restapirecipes.favorite.model

import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.user.model.User
import jakarta.persistence.*

@Entity
@Table(name = "user_favorite")
class UserFavorite(
    @EmbeddedId
    val id: UserFavoriteId = UserFavoriteId(),

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    val user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId")
    @JoinColumn(name = "recipe_id")
    val recipe: Recipe
)