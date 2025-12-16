package com.grig.restapirecipes.repository

import com.grig.restapirecipes.model.Recipe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface RecipeRepository : JpaRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {

    @Query("""
        SELECT r FROM Recipe r
        LEFT JOIN FETCH r.categories
        WHERE r.id = :id
    """)
    fun findRecipeBase(id: Long): Recipe?




//    Делаем ПОИСК одновременно по имени и ингредиенту - Рабочий
    @Query("""
        SELECT DISTINCT r FROM Recipe r
        LEFT JOIN r.recipeIngredients ri
        LEFT JOIN ri.ingredient i
        WHERE (:name IS NULL OR LOWER(COALESCE(r.name, '')) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:ingredient IS NULL OR LOWER(COALESCE(i.name, '')) LIKE LOWER(CONCAT('%', :ingredient, '%')))
    """)
    fun search(
        @Param("name") name: String?,
        @Param("ingredient") ingredient: String?
    ) : List<Recipe>

//// Был - либо имя рецепта, либо имя ингредиента
//    //    делаем ОДИН endpoint, но с разными query-параметрами:
////    GET /api/recipes?name=борщ
////GET /api/recipes?ingredient=картофель
//    @Query("""
//        SELECT DISTINCT r FROM Recipe r
//        LEFT JOIN r.categories c
//        WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))
//    """)
//    fun searchByName(name: String): List<Recipe>
//
//    @Query("""
//        SELECT DISTINCT r FROM Recipe r
//        JOIN r.recipeIngredients ri
//        JOIN ri.ingredient i
//        WHERE LOWER(i.name) LIKE LOWER(CONCAT('%', :ingredient, '%'))
//    """)
//    fun searchByIngredient(ingredient: String): List<Recipe>


//    fun findByNameContainingIgnoreCase(name: String): List<Recipe>
//
//    @Query("""
//        SELECT DISTINCT r FROM Recipe r
//        LEFT JOIN FETCH r.categories
//        LEFT JOIN FETCH r.recipeIngredients ri
//        LEFT JOIN FETCH ri.ingredient
//        LEFT JOIN FETCH r.steps
//    """)
//    fun findAllWithDetails(): List<Recipe>
//
//    @Query("""
//        SELECT DISTINCT r FROM Recipe r
//        LEFT JOIN FETCH r.categories
//        LEFT JOIN FETCH r.recipeIngredients ri
//        LEFT JOIN FETCH ri.ingredient
//        LEFT JOIN FETCH r.steps
//        WHERE r.id =:id
//    """)
//    fun findByIdWithDetails(id: Long): Recipe?
//

}