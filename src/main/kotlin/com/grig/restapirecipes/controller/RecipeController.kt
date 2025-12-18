package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.CreateRecipeRequest
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.service.RecipeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("api/recipes")
@Tag(name = "Recipes")
class RecipeController(
    private val recipeService : RecipeService,
    private val recipeMapper: RecipeMapper,
//    private val ingredientMapper: IngredientMapper
    ){

    @Operation(summary = "Получить рецепт по id")
    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: Long) : ResponseEntity<RecipeDto> {
        val recipeDto = recipeService.getRecipeById(id)
        return ResponseEntity.ok(recipeDto)
    }

////    Вернет рецепты отфильтрованные по названию или инградиенту, иначе все рецепты!!!
//    @GetMapping
//    fun searchRecipes(
//        @RequestParam(required = false) name: String?,
//        @RequestParam(required = false) ingredient: String?
//    ) : List<RecipeDto> =
//        recipeService.searchRecipe(name, ingredient)


    @Operation(summary = "Комбинированный поиск рецептов по названию и инградиенту, или все")
    @GetMapping("/search")
    fun search(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) ingredient: String?
    ) : List<RecipeDto> = recipeService.search(name, ingredient)


    //    вспомогательная функция в контроллер для получение Email (авторизированного пользователя)
    private fun getCurrentUserEmail() : String {
        val authentication = SecurityContextHolder.getContext().authentication
        return authentication?.name ?: throw IllegalArgumentException("User not authenticated")
    }

    @Operation(summary = "Создать новый рецепт")
    @PostMapping
    fun createRecipe(@RequestBody @Valid request: CreateRecipeRequest) : ResponseEntity<RecipeDto> {
        val userEmail = getCurrentUserEmail()
        val savedRecipe = recipeService.createRecipe(userEmail, request)
        val dto = recipeMapper.toDto(
            recipe = savedRecipe,
            ingredients = savedRecipe.recipeIngredients.toList(),
            steps = savedRecipe.steps.toList()
        )
        return ResponseEntity.status(HttpStatus.CREATED).body(dto)
    }

    @Operation(summary = "Редактировать рецепт")
    @PutMapping("/{id}")
    fun updateRecipe(
        @PathVariable id: Long,
        @RequestBody @Valid request: UpdateRecipeRequest
    ) : ResponseEntity<RecipeDto> {
        val updateRecipe = recipeService.updateRecipe(id, request)
        val dto = recipeMapper.toDto(
            recipe = updateRecipe,
            ingredients = updateRecipe.recipeIngredients.toList(),
            steps = updateRecipe.steps.toList()
        )
        return ResponseEntity.ok(dto)
    }

//    Что будет происходить:
//	•	Если рецепт с таким id есть — удаляется, возвращается 204.
//	•	Если рецепта нет — выбрасывается RecipeNotFoundException, который ловит GlobalExceptionHandler и возвращает красивый JSON 404.
//	•	Всё выполняется в одной транзакции — если что-то пойдёт не так, откатится автоматически.
    @Operation(summary = "Удалить рецепт")
    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: Long) : ResponseEntity<Unit> {
        recipeService.deleteRecipe(id)
        return ResponseEntity.noContent().build()    // 204 No Content
    }

    //    Pagination + Sort
//    @GetMapping("/recipes")
    @Operation(summary = "Get recipes with optional search, pagination and sorting  (с поиском, по странице и сортировкой)")
    @GetMapping
    fun searchPecipes(
        @RequestParam(required = false) name : String?,
        @RequestParam(required = false) ingredient: String?,
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "name") sortBy: String,
        @RequestParam(defaultValue = "ASC") direction: String
    ) : Page<RecipeDto> {
        return recipeService.searchRecipes(name, ingredient, page, size, sortBy, direction)
    }

}