package com.grig.restapirecipes.mapper

import com.grig.restapirecipes.dto.CategoryDto
import com.grig.restapirecipes.model.Category
import org.springframework.stereotype.Component

@Component
class CategoryMapper {

    fun toDto(entity: Category): CategoryDto =
        CategoryDto(
//            id = entity.id!!,   //  ???
            id = entity.id ?: error(("Category id is null")),
            name = entity.name
        )
}