package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.CreateRecipeRequest
import com.grig.restapirecipes.dto.RecipeDto
import com.grig.restapirecipes.dto.UpdateRecipeRequest
import com.grig.restapirecipes.mapper.IngredientMapper
import com.grig.restapirecipes.mapper.RecipeMapper
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.service.RecipeService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/recipes")
class RecipeController(
    private val recipeService : RecipeService,
    private val recipeMapper: RecipeMapper,
    private val ingredientMapper: IngredientMapper
    ){

//    @GetMapping
//    fun getAllRecipes() : List<RecipeDto> = recipeService.getAllRecipes()

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


    @GetMapping
    fun search(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) ingredient: String?
    ) : List<RecipeDto> = recipeService.search(name, ingredient)


    @PostMapping
    fun createRecipe(@RequestBody @Valid request: CreateRecipeRequest) : ResponseEntity<RecipeDto> {
        val savedRecipe = recipeService.createRecipe(request)
        val dto = recipeMapper.toDto(
            recipe = savedRecipe,
            ingredients = savedRecipe.recipeIngredients.toList(),
            steps = savedRecipe.steps.toList()
        )
        return ResponseEntity.ok(dto)
    }

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
    @DeleteMapping("/{id}")
    fun deleteRecipe(@PathVariable id: Long) : ResponseEntity<Unit> {
        recipeService.deleteRecipe(id)
        return ResponseEntity.noContent().build()    // 204 No Content
    }

}