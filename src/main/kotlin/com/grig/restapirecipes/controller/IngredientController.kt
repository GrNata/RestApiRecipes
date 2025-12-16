package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.IngredientDto
import com.grig.restapirecipes.repository.IngredientRepository
import com.grig.restapirecipes.service.IngredientService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/ingredients")
class IngredientController(
    private val ingredientService: IngredientService,
    private val ingredientRepository: IngredientRepository
) {

    @GetMapping("/all")
    fun getAll() : List<IngredientDto> = ingredientService.getAll()

    @GetMapping("/{id}")
    fun getById(id: Long) : IngredientDto = ingredientService.getById(id)


//    с пагинацией, сортировкой и фильтром
//      @Operation(summary = "...") описывает эндпоинт в Swagger UI.
    @Operation(summary = "Get ingredients with optional search, pagination and sorting")
    @GetMapping
    fun getIngredients(
        @RequestParam(required = false) name: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name") sortBy: String,
        @RequestParam(defaultValue = "ASC") direction: String
    ) : Page<IngredientDto> {

        val pageable = PageRequest.of(
            page,
            size,
            if (direction.uppercase() == "ASC") Sort.by(sortBy).ascending()
                   else Sort.by(sortBy)
        )
        val ingredients = if (!name.isNullOrBlank()) {
            ingredientRepository.findByNameContainingIgnoreCase(name, pageable)
            } else {
                ingredientRepository.findAll(pageable)
            }
        return ingredients.map { IngredientDto(requireNotNull(it.id), it.name) }
    }
}