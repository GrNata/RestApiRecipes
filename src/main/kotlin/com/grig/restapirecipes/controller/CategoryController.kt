package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.CategoryDto
import com.grig.restapirecipes.service.CategoryService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/categories")
class CategoryController(
    private val categoryService: CategoryService
) {

    @GetMapping
    fun getAll() : List<CategoryDto> = categoryService.getAll()

    @GetMapping("/{id}")
    fun getById(id: Long) : CategoryDto = categoryService.getById(id)
}