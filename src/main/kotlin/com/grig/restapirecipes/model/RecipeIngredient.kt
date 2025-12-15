package com.grig.restapirecipes.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "recipe_ingredient")
class RecipeIngredient(
    @EmbeddedId
    val id: RecipeIngredientId = RecipeIngredientId(),

    @ManyToOne
    @MapsId("recipeId") // связывает с id.recipeId
    @JoinColumn(name = "recipe_id")
    @JsonIgnore     // избегаем рекурсию
    val recipe: Recipe,

    @ManyToOne
    @MapsId("ingredientId") // связывает с id.ingredientId
    @JoinColumn(name = "ingredient_id")
    val ingredient: Ingredient,

    val amount: String?
) {
//    ????
    constructor(recipe: Recipe, ingredient: Ingredient, amount: String?) : this(
        id = RecipeIngredientId(recipe.id ?: 0, ingredient.id ?: 0),
        recipe = recipe,
        ingredient = ingredient,
        amount = amount
    )
}


@Embeddable
class RecipeIngredientId(
    @Column(name = "recipe_id")
    val recipeId: Long = 0,

    @Column(name = "ingredient_id")
    val ingredientId: Long = 0
) : Serializable
