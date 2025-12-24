package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.UnitEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UnitRepository : JpaRepository<UnitEntity, Long> {
}