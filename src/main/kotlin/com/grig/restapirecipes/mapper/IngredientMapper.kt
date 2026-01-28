package com.grig.restapirecipes.mapper

import com.grig.restapirecipes.dto.response.IngredientDto
import com.grig.restapirecipes.dto.response.IngredientWithAmountDto
import com.grig.restapirecipes.model.Ingredient
import com.grig.restapirecipes.model.RecipeIngredient
import org.springframework.stereotype.Component

@Component
class IngredientMapper {

    fun toDto(entity: Ingredient) : IngredientDto =
        IngredientDto(
            id = entity.id ?: error(("Ingredient id is null")),
            name = entity.name,
            nameEnglish = entity.nameEng,
            energyKcal100g = entity.energyKcal100g
        )

//    fun toIngredientWithAmountDto(ingredient: RecipeIngredient) : IngredientWithAmountDto {
//        IngredientWithAmountDto(
//            id = ingredient.id,
//            name = ingredient.name,
//            nameEng = ingredient.nameEng,
//
//        )
//    }
}