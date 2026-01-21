package com.grig.restapirecipes.mapper

import com.grig.restapirecipes.dto.response.CategoryDto
import com.grig.restapirecipes.dto.response.IngredientWithAmountDto
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.dto.response.UnitDto
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

//                categories = recipe.categories.map {
//                    CategoryDto(
//                        id = requireNotNull(it.id),
//                        name = it.name,
//                        image = it.image
//                    )
//                },

                categories = recipe.categories,

                ingredients = ingredients.map {
                    IngredientWithAmountDto(
                        id = requireNotNull(it.ingredient.id),
                        name = it.ingredient.name,
                        amount = it.amount,
                        unit = UnitDto(requireNotNull(it.unit.id), it.unit.code, it.unit.label)
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
                unit = UnitDto(requireNotNull(ri.unit.id), ri.unit.code, ri.unit.label)
            )
        }
}