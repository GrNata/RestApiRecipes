package com.grig.restapirecipes.controller.admin

import com.grig.restapirecipes.dto.request.IngredientRequest
import com.grig.restapirecipes.dto.response.IngredientDto
import com.grig.restapirecipes.service.IngredientService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/admin/ingredients")
@PreAuthorize("hasRole('ADMIN')")
class IngredientAdminController(
    val ingredientService: IngredientService
) {

    @GetMapping
    fun getAll(): List<IngredientDto> = ingredientService.getAllingredients()

    @PostMapping
    fun create(@RequestBody request: IngredientRequest) =
        ingredientService.createIngredient(request)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody request: IngredientRequest) {
        println("ADMIN: IngredientAdminController: START")
        println("ADMIN: IngredientAdminController: id=$id,  request: $request")

        ingredientService.updateIngredient(id, request)
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long) = ingredientService.deleteIngredient(id)
}