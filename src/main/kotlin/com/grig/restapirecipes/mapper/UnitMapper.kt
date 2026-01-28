package com.grig.restapirecipes.mapper

import com.grig.restapirecipes.dto.response.UnitDto
import com.grig.restapirecipes.model.UnitEntity
import org.springframework.stereotype.Component

@Component
class UnitMapper {

    fun UnitEntity.toDto(unit: UnitEntity) =
        UnitDto(
            id = unit.id,
            code = unit.code,
            label = unit.label
        )
}