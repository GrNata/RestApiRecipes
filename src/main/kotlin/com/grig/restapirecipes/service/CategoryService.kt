package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CategoryRequest
import com.grig.restapirecipes.dto.response.CategoryDto
import com.grig.restapirecipes.repository.CategoryRepository
import org.springframework.stereotype.Service
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.Category

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository
) {

    fun getAllCategories(): List<CategoryDto> =
        categoryRepository.findAll().map {
            CategoryDto(requireNotNull(it.id), it.name, it.image)
        }

    fun getByIdCategory(id: Long) : CategoryDto =
        categoryRepository.findById(id)
            .map {
                CategoryDto(requireNotNull(it.id), it.name, it.image)
            }
            .orElseThrow { RecipeNotFoundException("Category with id ${id} not found.") }

    fun createCategory(request: CategoryRequest) : Category {
        val category = Category(name = request.name, image = request.image)
        return categoryRepository.save(category)
    }

    fun updateCategory(id: Long, request: CategoryRequest) : Category {
        val category = categoryRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Category with id ${id} not found.") }
        category.name = request.name
        category.image = request.image
        return categoryRepository.save(category)
    }

    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Category with id {id} not found.") }
        categoryRepository.delete(category)
    }
}