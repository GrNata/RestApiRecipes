package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.Category
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoryRepository : JpaRepository<Category, Long> {

    fun findByName(name: String) : Category?

    // Пагинация и поиск по подстроке
    //    можем подгружать данные частями и фильтровать по части имени
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable) : Page<Category>
}