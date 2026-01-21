package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.CategoryType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryTypeRepository : JpaRepository<CategoryType, Long> {
}