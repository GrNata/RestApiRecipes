package com.grig.restapirecipes.controller.admin

import com.grig.restapirecipes.dto.request.CategoryTypeRequest
import com.grig.restapirecipes.service.CategoryTypeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/categories-types")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "CategoryType", description = "ADMIN - Методы для работы с типами категорий")
class CategoryTypeAdminController(
    val categoryTypeService: CategoryTypeService
) {
    @Operation(summary = "ADMIN - Получить все типы категорий")
    @GetMapping
    fun getAllCategoryTypes() = categoryTypeService.getAllCategoryTypes()

    @Operation(summary = "ADMIN - Создать тип категории")
    @PostMapping
    fun createCategoryTypes(@RequestBody request: CategoryTypeRequest) =
        categoryTypeService.createCategoryType(request)

    @Operation(summary = "ADMIN - Редактировать тип категории")
    @PutMapping("/{id}")
    fun updateCategoryType(@PathVariable id: Long, @RequestBody request: CategoryTypeRequest) =
        categoryTypeService.updateCategoryType(id, request)

    @Operation(summary = "Удалить тип категории")
    @DeleteMapping("/{id}")
    fun deleteCategoryType(@PathVariable id: Long) = categoryTypeService.deleteCategoryType(id)
}