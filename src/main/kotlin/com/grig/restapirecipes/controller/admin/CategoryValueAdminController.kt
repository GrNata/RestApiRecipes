package com.grig.restapirecipes.controller.admin

import com.grig.restapirecipes.dto.request.CategoryValueRequest
import com.grig.restapirecipes.service.CategoryValueService
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
@RequestMapping("/api/admin/category-values")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "CategoryValue", description = "ADMIN - Методы для работы с описанием категорий")
class CategoryValueAdminController(
    val categoryValueService: CategoryValueService
) {
    @Operation(summary = "ADMIN - Получить все названия категорий")
    @GetMapping
    fun getAllCategoryValue() = categoryValueService.getAllCategoryValues()

    @Operation(summary = "ADMIN - Создать название категории")
    @PostMapping
    fun createCategoryValue(@RequestBody request: CategoryValueRequest) =
        categoryValueService.createCategoryValue(request)

    @Operation(summary = "ADMIN - Редактировать название категории")
    @PutMapping("/{id}")
    fun updateCategoryValue(@PathVariable id : Long, @RequestBody request: CategoryValueRequest) =
        categoryValueService.updateCategoryValue(id, request)

    @Operation(summary = "ADMIN - Удалить название категории")
    @DeleteMapping("/{id}")
    fun deleteCategoryValue(@PathVariable id: Long) = categoryValueService.deleteCategoryValue(id)
}