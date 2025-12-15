package com.grig.restapirecipes.mapper

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
                categories = recipe.categories.map { it.name },
                ingredients = ingredients.map {
                    "${it.ingredient.name} ${it.amount}"
                },
                steps = steps.map { it.description }
            )

//    fun toDto(recipe: Recipe) : RecipeDto {
//        val categoriesList = recipe.categories.toList()
//        val ingredientsList = recipe.recipeIngredients.toList()
//
//        return RecipeDto(
//            id = requireNotNull(recipe.id),
//            name = recipe.name,
//            description = recipe.description,
//            image = recipe.image,
//            categories = categoriesList.map { it.name },
////            ingredients = recipe.toIngredientWithAmountDto(),
//            ingredients = ingredientsList.map { "${it.ingredient.name} ${it.amount}" },
////            steps = recipe.steps.toList().sortedBy { it.stepNumber }.map { it.description }
//            steps = recipe.steps
//                .sortedBy { it.stepNumber }
//                .map { it.description }
//        )
//    }

    fun Recipe.toIngredientWithAmountDto() : List<IngredientWithAmountDto> =
        recipeIngredients.map { ri ->
            IngredientWithAmountDto(
                id = requireNotNull(ri.ingredient.id),
                name = ri.ingredient.name,
                amount = ri.amount
            )
        }

//    fun toEntity(dto: RecipeDto, category: Category?, ingredients: List<Ingredient>) : Recipe =
//        Recipe(
//            name = dto.name,
//            description = dto.description,
//            image = dto.image,
//            category = category,
//            ingredients = ingredients.toMutableList(),
//            steps = dto.steps.mapIndexed { index, text ->
//                CookingStep(stepNumber = index + 1, description =  text)
//            }.toMutableList()
//        )
}