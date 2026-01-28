package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.Ingredient
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IngredientRepository : JpaRepository<Ingredient, Long> {

    fun findByName(name: String): Ingredient?

    // Пагинация и поиск по подстроке
//    можем подгружать данные частями и фильтровать по части имени
    fun findByNameContainingIgnoreCase(name: String, pageable: Pageable) : Page<Ingredient>

    fun findByNameEngIgnoreCase(nameEng: String): Ingredient?
}