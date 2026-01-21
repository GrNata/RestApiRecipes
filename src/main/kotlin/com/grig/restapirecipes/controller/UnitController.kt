package com.grig.restapirecipes.controller

import com.grig.restapirecipes.dto.request.UnitRequest
import com.grig.restapirecipes.dto.response.UnitDto
import com.grig.restapirecipes.service.UnitService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/units")
@Tag(name = "Units", description = "Методы для работыт с единицами измерения")
class UnitController(
    private val unitService: UnitService
) {

    @Operation(summary = "Получить все единицы измерения")
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    fun getUnits(): List<UnitDto> {
        return unitService.getAllUnits()
    }

    @Operation(summary = "Получить единицу измерения по id")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUnitById(@PathVariable id: Long): ResponseEntity<UnitDto> {
        val unit =  unitService.getByIdUnit(id)
        return ResponseEntity.ok(unit)
    }

    @Operation(summary = "Создать единицу измерения")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    fun createUnit(@RequestBody @Valid request: UnitRequest) = unitService.createUnit(request)


    @Operation(summary = "Редактировать единицу измерения по id")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun updateUnit(@PathVariable id: Long, @RequestBody @Valid request: UnitRequest) =
        unitService.updateUnit(id, request)

    @Operation(summary = "Удалить единицу измерения по id")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUnit(@PathVariable id: Long) = unitService.deleteUnit(id)
}