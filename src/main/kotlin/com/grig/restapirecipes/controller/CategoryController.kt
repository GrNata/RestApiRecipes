package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.CategoryRequest
import com.grig.restapirecipes.dto.response.CategoryDto
import com.grig.restapirecipes.repository.CategoryRepository
import com.grig.restapirecipes.service.CategoryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/categories")
@Tag(name = "Category", description = "Методы для работы с категориями")
class CategoryController(
    private val categoryService: CategoryService,
    private val categoryRepository: CategoryRepository
) {

    @Operation(summary = "Получить все категории")
    @GetMapping("/all")
    fun getAll() : List<CategoryDto> = categoryService.getAllCategories()


    @Operation(summary = "Получить категорию по id")
    @GetMapping("/{id}")
    fun getById(id: Long) : CategoryDto = categoryService.getByIdCategory(id)


//    с пагинацией, сортировкой и фильтром
//      @Operation(summary = "...") описывает эндпоинт в Swagger UI.
    @Operation(summary = "Get category with optional search, padination and sorting (с поиском, по странице и сортировкой)")
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

    @Operation(summary = "Создать категорию")
    @PostMapping
    fun create(@RequestBody request: CategoryRequest) = categoryService.createCategory(request)

    @Operation(summary = "Редактировать категорию")
    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: CategoryRequest) = categoryService.updateCategory(id, request)

    @Operation(summary = "Удалить категорию")
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) {
        categoryService.deleteCategory(id)
    }
}