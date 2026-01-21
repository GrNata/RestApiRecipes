package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.request.UnitRequest
import com.grig.restapirecipes.dto.response.UnitDto
import com.grig.restapirecipes.exception.RecipeNotFoundException
import com.grig.restapirecipes.model.UnitEntity
import com.grig.restapirecipes.repository.UnitRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UnitService(private val unitRepository: UnitRepository) {

    @Transactional(readOnly = true)
    fun getAllUnits(): List<UnitDto> {
        return unitRepository.findAll()
            .map {
                UnitDto(requireNotNull(it.id), it.code, it.label)
            }
    }

    fun getByIdUnit(id: Long): UnitDto {
        return unitRepository.findById(id)
            .map {
                UnitDto(requireNotNull(it.id), it.code, it.label)
            }
            .orElseThrow { RecipeNotFoundException("Unit with id ${id} not found.") }
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun createUnit(request: UnitRequest): UnitEntity {
        val unit = UnitEntity(request.code, request.label)
        return unitRepository.save(unit)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun updateUnit(id: Long, request: UnitRequest): UnitEntity {
        val unit = unitRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Unit with id ${id} not found.") }
        unit.code = request.code
        unit.label = request.label
        return unitRepository.save(unit)
    }

    @PreAuthorize("hasRole('ADMIN')")
    fun deleteUnit(id: Long) {
        val unit = unitRepository.findById(id)
            .orElseThrow { RecipeNotFoundException("Unit with id ${id} not found.") }
        unitRepository.delete(unit)
    }
}