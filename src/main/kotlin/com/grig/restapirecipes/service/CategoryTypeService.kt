package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CategoryRequest
import com.grig.restapirecipes.dto.request.CategoryTypeRequest
import com.grig.restapirecipes.dto.response.CategoryTypeDto
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.Category
import com.grig.restapirecipes.model.CategoryType
import com.grig.restapirecipes.repository.CategoryTypeRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class CategoryTypeService(
    private val categoryTypeRepository: CategoryTypeRepository
) {

    fun getAllCategoryTypes() : List<CategoryTypeDto> =
        categoryTypeRepository.findAll().map {
            CategoryTypeDto(
                id = requireNotNull(it.id),
                nameType = it.nameType
            )
        }

    fun getByIdCategoryType(id: Long) : CategoryTypeDto =
        categoryTypeRepository.findById(id).map {
            CategoryTypeDto(
                id = requireNotNull(it.id),
                nameType = it.nameType
            )
        }
            .orElseThrow { RecipeNotFoundException("CategoryType with id ${id} not found.") }

    @PreAuthorize("hasRole('ADMIN')")
    fun createCategoryType(request: CategoryTypeRequest) : CategoryTypeDto {
        val categoryType = CategoryType(nameType = request.nameType)
        val saved = categoryTypeRepository.save(categoryType)

        return CategoryTypeDto(
            id = requireNotNull(saved.id),
            nameType = saved.nameType
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateCategoryType(id: Long, request: CategoryTypeRequest) : CategoryTypeDto {
        val categoryType = categoryTypeRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("CategoryType with id ${id} not found.") }
        categoryType.nameType = request.nameType
        val updated = categoryTypeRepository.save(categoryType)

        return CategoryTypeDto(
            id = requireNotNull(updated.id),
            nameType = updated.nameType
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategoryType(id: Long) {
        val categoryType = categoryTypeRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("CategoryType with id {id} not found.") }
        categoryTypeRepository.delete(categoryType)
    }

}