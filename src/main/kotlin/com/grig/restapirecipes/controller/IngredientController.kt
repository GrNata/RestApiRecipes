package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.IngredientRequest
import com.grig.restapirecipes.dto.response.IngredientDto
import com.grig.restapirecipes.repository.IngredientRepository
import com.grig.restapirecipes.service.IngredientService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/ingredients")
@Tag(name = "Ingredient", description = "Методы для работыт с ингредиентами")
class IngredientController(
    private val ingredientService: IngredientService,
    private val ingredientRepository: IngredientRepository
) {

    @Operation(summary = "Получить все инградиент")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    fun getAll() : List<IngredientDto> = ingredientService.getAllingredients()

    @Operation(summary = "Получить инградиент по id")
    @GetMapping("/{id}")
    fun getById(id: Long) : IngredientDto = ingredientService.getByIdIngredient(id)


//    с пагинацией, сортировкой и фильтром
//      @Operation(summary = "...") описывает эндпоинт в Swagger UI.
    @Operation(summary = "Get ingredients with optional search, pagination and sorting  (с поиском, по странице и сортировкой)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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

    @Operation(summary = "Создать инградиент")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody request: IngredientRequest) = ingredientService.createIngredient(request)

    @Operation(summary = "Редактировать инградиент")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun update(@PathVariable id: Long, @RequestBody request: IngredientRequest) =
        ingredientService.updateIngredient(id, request)

    @Operation(summary = "Удалить инградиент")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Long) {
        ingredientService.deleteIngredient(id)
    }
}