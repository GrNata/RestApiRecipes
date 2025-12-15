package com.grig.restapirecipes.mapper

import com.grig.restapirecipes.dto.CategoryDto
import com.grig.restapirecipes.dto.IngredientWithAmountDto
import com.grig.restapirecipes.dto.RecipeDto
import com.grig.restapirecipes.model.CookingStep
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.model.RecipeIngredient
import org.springframework.stereotype.Component

//Используем object как синглтон mapper — удобно и без сторонних библиотек.
@Component
object RecipeMapper {

        fun toDto(
            recipe: Recipe,
            ingredients: List<RecipeIngredient>,
            steps: List<CookingStep>
        ): RecipeDto =
            RecipeDto(
                id = recipe.id!!,
                name = recipe.name,
                description = recipe.description,
                image = recipe.image,

                categories = recipe.categories.map {
                    CategoryDto(
                        id = requireNotNull(it.id),
                        name = it.name,
                        image = it.image
                    )
                },

                ingredients = ingredients.map {
                    IngredientWithAmountDto(
                        id = requireNotNull(it.ingredient.id),
                        name = it.ingredient.name,
                        amount = it.amount,
                        unit = it.ingredient.unit
                    )
                },

                steps = steps.map { it.description }
            )

    fun Recipe.toIngredientWithAmountDto() : List<IngredientWithAmountDto> =
        recipeIngredients.map { ri ->
            IngredientWithAmountDto(
                id = requireNotNull(ri.ingredient.id),
                name = ri.ingredient.name,
                amount = ri.amount,

                unit = ri.ingredient.unit
            )
        }
}