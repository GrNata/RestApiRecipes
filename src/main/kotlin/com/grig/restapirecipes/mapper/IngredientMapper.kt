package com.grig.restapirecipes.mapper

import com.grig.restapirecipes.dto.IngredientDto
import com.grig.restapirecipes.model.Ingredient
import org.springframework.stereotype.Component

@Component
class IngredientMapper {

    fun toDto(entity: Ingredient) : IngredientDto =
        IngredientDto(
//            id = entity.id!!,   //  ???
            id = entity.id ?: error(("Ingredient id is null")),
//            id = requireNotNull(entity.id)
            name = entity.name
        )
}