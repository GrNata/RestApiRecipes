package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.CategoryValue
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryValueRepository : JpaRepository<CategoryValue, Long> {
}