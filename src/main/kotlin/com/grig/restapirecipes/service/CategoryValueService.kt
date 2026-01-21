package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.CategoryTypeRequest
import com.grig.restapirecipes.dto.request.CategoryValueRequest
import com.grig.restapirecipes.dto.response.CategoryValueDto
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.CategoryType
import com.grig.restapirecipes.model.CategoryValue
import com.grig.restapirecipes.repository.CategoryTypeRepository
import com.grig.restapirecipes.repository.CategoryValueRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class CategoryValueService(
    private val categoryValueRepository: CategoryValueRepository,
    private val categoryTypeRepository: CategoryTypeRepository
) {

    fun getAllCategoryValues() : List<CategoryValueDto> =
        categoryValueRepository.findAll().map {
            CategoryValueDto(
                id = requireNotNull(it.id),
                typeId = requireNotNull(it.categoryType.id),
                typeName = it.categoryType.nameType,
                categoryValue = it.categoryValue
            )
        }

    fun getByIdCategoryValue(id: Long): CategoryValueDto =
        categoryValueRepository.findById(id).map {
            CategoryValueDto(
                id = requireNotNull(it.id),
                typeId = requireNotNull(it.categoryType.id),
                typeName = it.categoryType.nameType,
                categoryValue = it.categoryValue
            )
        }.orElseThrow { RecipeNotFoundException("CategoryValue with id ${id} not found.") }

    @PreAuthorize("hasRole('ADMIN')")
    fun createCategoryValue(request: CategoryValueRequest) : CategoryValueDto {
        val type = categoryTypeRepository.findById(request.categoryTypeId)
            .orElseThrow { RecipeNotFoundException("CategoryType not found")}
        val categoryValue = CategoryValue(
            categoryType = type,
            categoryValue = request.categoryValue
        )
        val saved = categoryValueRepository.save(categoryValue)

        return CategoryValueDto(
            id = requireNotNull(saved.id),
            typeId = requireNotNull(saved.categoryType.id),
            typeName = saved.categoryType.nameType,
            categoryValue = saved.categoryValue
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateCategoryValue(id: Long, request: CategoryValueRequest) : CategoryValueDto {
        val categoryValue = categoryValueRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("CategoryValue with id ${id} not found.") }

        val type = categoryTypeRepository.findById(request.categoryTypeId)
            .orElseThrow { RecipeNotFoundException("CategoryType not found")}

        categoryValue.categoryType = type
        categoryValue.categoryValue = request.categoryValue
        val updated =  categoryValueRepository.save(categoryValue)

        return CategoryValueDto(
            id = requireNotNull(updated.id),
            typeId = requireNotNull(updated.categoryType.id),
            typeName = updated.categoryType.nameType,
            categoryValue = updated.categoryValue
        )
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteCategoryValue(id: Long) {
        val categoryValue = categoryValueRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("CategoryValue with id {id} not found.") }
        categoryValueRepository.delete(categoryValue)
    }
}