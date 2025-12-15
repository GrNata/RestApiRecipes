package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.RecipeDto
import com.grig.restapirecipes.service.RecipeService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/recipes")
class RecipeController(private val recipeService : RecipeService){

//    @GetMapping
//    fun getAllRecipes() : List<RecipeDto> = recipeService.getAllRecipes()

    @GetMapping("/{id}")
    fun getRecipeById(@PathVariable id: Long) : ResponseEntity<RecipeDto> {
        val recipeDto = recipeService.getRecipeById(id)
        return ResponseEntity.ok(recipeDto)
    }

//    Вернет рецепты отфильтрованные по названию или инградиенту, иначе все рецепты!!!
    @GetMapping
    fun searchrecipes(
        @RequestParam(required = false) name: String?,
        @RequestParam(required = false) ingredient: String?
    ) : List<RecipeDto> =
        recipeService.searchRecipe(name, ingredient)



//    @PostMapping
//    fun create(@RequestBody recipe: RecipeDto) : RecipeDto = recipeService.create(recipe)
//
//    @PutMapping("/{id}")
//    fun update(
//        @PathVariable id : Long,
//        @RequestBody updateRecipe: RecipeDto
//    ) : RecipeDto {
//        return recipeService.update(id, updateRecipe)
//    }
//
//    @DeleteMapping("/{id}")
//    fun delete(@PathVariable id: Long) {
//        recipeService.delete(id)
//    }
//
//    @GetMapping("/search")
//    fun search(
//        @RequestParam(required = false) name: String?,
//        @RequestParam(required = false) ingredient: String?
//    ) : List<RecipeDto> {
//        return  when {
//            name != null -> recipeService.searchByName(name)
//            ingredient != null -> recipeService.searchByIngredient(ingredient)
//            else -> emptyList()
//        }
//    }
//
////    Загрузка изображений
//    @PostMapping("/{id}/image")
//fun uploadImage(
//    @PathVariable id: Long,
//    @RequestParam("file") file: MultipartFile
//    ) : RecipeDto {
//    return recipeService.uploadImage(id, file)
//}

}