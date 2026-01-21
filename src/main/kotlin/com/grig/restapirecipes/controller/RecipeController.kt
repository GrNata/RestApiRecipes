package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.CreateRecipeRequest
import com.grig.restapirecipes.dto.response.RecipeDto
import com.grig.restapirecipes.dto.request.UpdateRecipeRequest
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.service.RecipeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.lang.IllegalArgumentException

@RestController
@RequestMapping("api/recipes")
@Tag(name = "Recipes", description = "Методы для работы с рецептами")
class RecipeController(
    private val recipeService : RecipeService,
    private val recipeMapper: RecipeMapper
    ){

    @Operation(summary = "Получить рецепты текущего пользователя (Мои рецепты)")
    @GetMapping("/my/recipes")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    fun getMyRecipes(
        @AuthenticationPrincipal user: UserDetails,
//        pageable: Pageable
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
        @RequestParam(defaultValue = "id") sortBy: String,
        @RequestParam(defaultValue = "DESC") direction: String
    ) : Page<RecipeDto> {

        // Проверяем, что sortBy — одно из допустимых полей сущности Recipe
        val safeSortBy = when (sortBy) {
            "id", "name" -> sortBy
            else -> "id" // любое другое поле заменяем на id
        }

        val pageable = PageRequest.of(page, size, Sort.Direction.fromString(direction), safeSortBy)

        // Получаем Page<Recipe> из сервиса
        val recipesPage = recipeService.getMyRecipes(user.username, pageable)

        // Преобразуем в Page<RecipeDto>
        return recipeService.getMyRecipes(user.username, pageable)
    }

    @Operation(summary = "Получить рецепт по id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.CREATED)
    fun createRecipe(@RequestBody @Valid request: CreateRecipeRequest) : ResponseEntity<RecipeDto> {

        println("Создание нового рецепта request: ${request}")
        val auth = SecurityContextHolder.getContext().authentication
        println("Создание нового рецепта auth: ${auth}")

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
    @ResponseStatus(HttpStatus.OK)
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRecipe(@PathVariable id: Long) : ResponseEntity<Unit> {
        recipeService.deleteRecipe(id)
        return ResponseEntity.noContent().build()    // 204 No Content
    }

    //    Pagination + Sort
//    @GetMapping("/recipes")
    @Operation(summary = "Get recipes with optional search, pagination and sorting  (с поиском, по странице и сортировкой)")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
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