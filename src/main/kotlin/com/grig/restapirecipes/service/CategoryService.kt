package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.response.CategoryDto
import com.grig.restapirecipes.repository.CategoryRepository
import org.springframework.stereotype.Service
import com.grig.restapirecipes.exception.RecipeNotFoundException

@Service
class CategoryService(
    private val categoryRepositpry: CategoryRepository
) {

    fun getAll(): List<CategoryDto> =
        categoryRepositpry.findAll().map {
            CategoryDto(requireNotNull(it.id), it.name, it.image)
        }

    fun getById(id: Long) : CategoryDto =
        categoryRepositpry.findById(id)
            .map {
                CategoryDto(requireNotNull(it.id), it.name, it.image)
            }
            .orElseThrow { RecipeNotFoundException("Category with id ${id} not found.") }

}