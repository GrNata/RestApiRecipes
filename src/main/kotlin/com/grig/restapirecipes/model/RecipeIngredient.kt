package com.grig.restapirecipes.model

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import java.io.Serializable

@Entity
@Table(name = "recipe_ingredient")
class RecipeIngredient(
    @EmbeddedId
    val id: RecipeIngredientId = RecipeIngredientId(),
//    val id: RecipeIngredientId,

//    @ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("recipeId") // связывает с id.recipeId
    @JoinColumn(name = "recipe_id")
    @JsonIgnore     // избегаем рекурсию
    val recipe: Recipe,

//    @ManyToOne
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("ingredientId") // связывает с id.ingredientId
    @JoinColumn(name = "ingredient_id")
    val ingredient: Ingredient,

    val amount: String?

) {
    /** ✅ УДОБНЫЙ фабричный конструктор */
    constructor(recipe: Recipe, ingredient: Ingredient, amount: String?) : this(
        id = RecipeIngredientId(
            recipeId = requireNotNull(recipe.id),
            ingredientId = requireNotNull(ingredient.id)
        ),
        recipe = recipe,
        ingredient = ingredient,
        amount = amount
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RecipeIngredient) return false
        return id ==other.id
    }

    override fun hashCode(): Int = id.hashCode()
}



@Embeddable
class RecipeIngredientId(
    @Column(name = "recipe_id")
    val recipeId: Long = 0,

    @Column(name = "ingredient_id")
    val ingredientId: Long = 0
) : Serializable
