package com.grig.restapirecipes.model

import jakarta.persistence.*

@Entity
@Table(name = "recipe")
class Recipe(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

//    @Column(nullable = false, columnDefinition = "VARCHAR(255)")
    @Column(nullable = false)
    var name: String,

    @Column(columnDefinition = "TEXT")
    var description: String? = null,

//    @Column(length = 500)
    var image: String? = null,

    @ManyToMany
    @JoinTable(
        name = "recipe_category",
        joinColumns = [JoinColumn(name = "recipe_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")]
    )
    var categories: MutableSet<Category> = mutableSetOf(),

//    @ManyToMany
//    @JoinTable(
//        name = "recipe_ingredient",
//        joinColumns = [JoinColumn(name = "recipe_id")],
//        inverseJoinColumns = [JoinColumn(name = "ingredient_id")]
//    )
//    var ingredients: MutableSet<Ingredient> = mutableSetOf(),

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @OrderBy("stepNumber ASC")
    var steps: MutableList<CookingStep> = mutableListOf(),

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true    // orphanRemoval = true — это автоматическое и безопасное удаление дочерних записей, когда ты убираешь их из коллекции родителя
    )
    val recipeIngredients: MutableSet<RecipeIngredient> = mutableSetOf()
)

//  orphanRemoval = true
//  Что Hibernate делает:
//Действие                              Результат
//recipe.recipeIngredients.clear()      Hibernate делает DELETE FROM recipe_ingredient ...
//recipe.recipeIngredients.remove(x)    Удаляет строку в БД, не просто из памяти
//recipeRepository.delete(recipe)       Удаляет рецепт → автоматически удаляет все RecipeIngredient

// Если есть orphanRemoval = true — НЕ надо вручную удалять детей через репозиторий
//Если ты удаляешь RecipeIngredient из recipe.recipeIngredients, JPA автоматически выполнит DELETE по таблице recipe_ingredient.
//	•	Если бы orphanRemoval = false, удаление из коллекции не удаляло бы запись из базы — нужно было бы делать repository.delete(...) вручную.
//	•	Работает только для @OneToMany или @OneToOne, не для @ManyToMany.
