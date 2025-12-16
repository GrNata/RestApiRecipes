package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.IngredientDto
import com.grig.restapirecipes.service.IngredientService
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("api/ingredients")
class IngredientController(
    private val ingredientService: IngredientService
) {

    @GetMapping
    fun getAll() : List<IngredientDto> = ingredientService.getAll()

    @GetMapping("/{id}")
    fun getById(id: Long) : IngredientDto = ingredientService.getById(id)
}