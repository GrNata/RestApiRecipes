package com.grig.restapirecipes.service

import com.grig.restapirecipes.model.Ingredient
import com.grig.restapirecipes.model.Recipe
import com.grig.restapirecipes.model.RecipeIngredient
import org.springframework.data.jpa.domain.Specification
import jakarta.persistence.criteria.*

//Specification для поиска - Делаем ПОИСК одновременно по имени и ингредиенту - Рабочий

object RecipeSpecification {

    fun hasName(name: String) : Specification<Recipe> {
        return Specification { root, _, cb ->
            cb.like(cb.lower(root.get("name")), "${name.lowercase()}%")
        }
    }

    fun hasIngredient(ingredient: String) : Specification<Recipe> {
        return Specification { root, _, cb ->
//            cb.like(cb.lower(join.get("name")), "%${ingredient.lowercase()}%")
            val join: Join<Recipe, RecipeIngredient> = root.join("recipeIngredients", JoinType.LEFT)
            val ingredientJoin: Join<RecipeIngredient, Ingredient> = join.join("ingredient", JoinType.LEFT)
            cb.like(cb.lower(ingredientJoin.get("name")), "%${ingredient.lowercase()}%")
        }
    }
}