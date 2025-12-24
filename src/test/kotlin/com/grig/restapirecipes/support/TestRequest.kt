package com.grig.restapirecipes.support

import com.grig.restapirecipes.dto.request.UpdateRecipeIngredientRequest
import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
import com.grig.restapirecipes.model.Recipe

object TestRequest {

    fun validUpdateRecipeRequest(
        name: String = "Update recipe",
        description: String = "Updated description"
    ) = UpdateRecipeRequest(
        name = name,
        description = description,
        image = null,
        categoryIds = listOf(1L),
        ingredients = listOf(
            UpdateRecipeIngredientRequest(
                ingredientId = 1L,
                amount = "100 g",
                unitId = 1L
            )
        ),
        steps = listOf("Step 1", "Step 2")
    )

//    fun validUpdateRecipe(
//        name: String = "Update recipe",
//        description: String = "Updated description"
//    ) = Recipe(
//        name = name,
//        description = description,
//        image = null,
//        categoryIds = listOf(1L),
//        ingredients = listOf(
//            UpdateRecipeIngredientRequest(
//                ingredientId = 1L,
//                amount = "100 g"
//            )
//        ),
//        steps = listOf("Step 1", "Step 2")
//    )

}