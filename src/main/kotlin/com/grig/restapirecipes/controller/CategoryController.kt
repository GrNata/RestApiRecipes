package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.CategoryDto
import com.grig.restapirecipes.repository.CategoryRepository
import com.grig.restapirecipes.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/categories")
class CategoryController(
    private val categoryService: CategoryService,
    private val categoryRepository: CategoryRepository
) {

    @GetMapping("/all")
    fun getAll() : List<CategoryDto> = categoryService.getAll()

    @GetMapping("/{id}")
    fun getById(id: Long) : CategoryDto = categoryService.getById(id)


//    с пагинацией, сортировкой и фильтром
//      @Operation(summary = "...") описывает эндпоинт в Swagger UI.
    @Operation(summary = "Get category with optional search, padination and sorting")
    @GetMapping
    fun getCategories(
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name") sortBy: String,
        @RequestParam(defaultValue = "ASC") ditection: String
    ) : Page<CategoryDto> {

        val pageable = PageRequest.of(
            page,
            size,
            if (ditection.uppercase() == "ASC") Sort.by(sortBy).ascending()
                  else Sort.by(sortBy).descending()
        )
        val categories = if (!name.isNullOrBlank()) {
            categoryRepository.findByNameContainingIgnoreCase(name, pageable)
        } else {
            categoryRepository.findAll(pageable)
        }
        return categories.map { CategoryDto(requireNotNull(it.id), it.name, it.image) }
    }
}