package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.CategoryTypeRequest
import com.grig.restapirecipes.dto.request.CategoryValueRequest
import com.grig.restapirecipes.dto.response.CategoryTypeDto
import com.grig.restapirecipes.dto.response.CategoryValueDto
import com.grig.restapirecipes.service.CategoryValueService
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
@RequestMapping("api/category-value")
@Tag(name = "CategoryValue", description = "Методы для работы с описанием категорий")
class CategoryValueController(
    private val categoryValueService: CategoryValueService
) {

    @Operation(summary = "Получить все названия категорий")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getAllCategoryValues() : List<CategoryValueDto> = categoryValueService.getAllCategoryValues()

    @Operation(summary = "Получить название категории по id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getById(@PathVariable id: Long) : CategoryValueDto =
        categoryValueService.getByIdCategoryValue(id)

    @Operation(summary = "Создать название категории")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: CategoryValueRequest): CategoryValueDto =
        categoryValueService.createCategoryValue(request)

    @Operation(summary = "Редактировать название категории")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: Long, @Valid @RequestBody request: CategoryValueRequest): CategoryValueDto =
        categoryValueService.updateCategoryValue(id, request)

    @Operation(summary = "Удалить название категории")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        categoryValueService.deleteCategoryValue(id)
    }
}