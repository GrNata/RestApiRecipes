package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CategoryRequest
import com.grig.restapirecipes.dto.response.CategoryDto
import com.grig.restapirecipes.repository.CategoryRepository
import org.springframework.stereotype.Service
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.Category
import org.springframework.data.domain.Pageable
import org.springframework.security.access.prepost.PreAuthorize

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

    @PreAuthorize("hasRole('ADMIN')")
    fun createCategory(request: CategoryRequest) : Category {
        val category = Category(name = request.name, image = request.image)
        return categoryRepository.save(category)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateCategory(id: Long, request: CategoryRequest) : Category {
        val category = categoryRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Category with id ${id} not found.") }
        category.name = request.name
        category.image = request.image
        return categoryRepository.save(category)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Category with id {id} not found.") }
        categoryRepository.delete(category)
    }
}