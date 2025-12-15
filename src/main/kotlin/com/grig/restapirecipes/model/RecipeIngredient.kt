package com.grig.restapirecipes.model

import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "recipe_ingredient")
data class RecipeIngredient(
    @EmbeddedId
    val id: RecipeIngredientId = RecipeIngredientId(),

    @ManyToOne
    @MapsId("recipeId") // связывает с id.recipeId
    @JoinColumn(name = "recipe_id")
    val recipe: Recipe,

    @ManyToOne
    @MapsId("ingredientId") // связывает с id.ingredientId
    @JoinColumn(name = "ingredient_id")
    val ingredient: Ingredient,

    val amount: String?
)


@Embeddable
data class RecipeIngredientId(
    @Column(name = "recipe_id")
    val recipeId: Long = 0,

    @Column(name = "ingredient_id")
    val ingredientId: Long = 0
) : Serializable
