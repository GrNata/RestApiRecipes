package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.CategoryRequest
import com.grig.restapirecipes.dto.request.CategoryTypeRequest
import com.grig.restapirecipes.dto.response.CategoryDto
import com.grig.restapirecipes.dto.response.CategoryTypeDto
import com.grig.restapirecipes.service.CategoryService
import com.grig.restapirecipes.service.CategoryTypeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/category-type")
@Tag(name = "CategoryType", description = "Методы для работы с типами категорий")
class CategoryTypeController(
    private val categoryTypeService: CategoryTypeService
) {

    @Operation(summary = "Получить все типы категорий")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCategoryTypes() : List<CategoryTypeDto> = categoryTypeService.getAllCategoryTypes()

    @Operation(summary = "Получить тип категории по id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: Long) : CategoryTypeDto =
        categoryTypeService.getByIdCategoryType(id)

    @Operation(summary = "Создать тип категории")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CategoryTypeRequest) : CategoryTypeDto =
        categoryTypeService.createCategoryType(request)

    @Operation(summary = "Редактировать тип категории")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: Long, @Valid @RequestBody request: CategoryTypeRequest) : CategoryTypeDto =
        categoryTypeService.updateCategoryType(id, request)

    @Operation(summary = "Удалить тип категории")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        categoryTypeService.deleteCategoryType(id)
    }
}