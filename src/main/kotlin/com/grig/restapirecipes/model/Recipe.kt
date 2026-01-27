package com.grig.restapirecipes.model

import com.grig.restapirecipes.user.model.User
import jakarta.persistence.*
import jakarta.validation.constraints.Min

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

//    кол-во порций
    @Min(1)
    @Column(name = "base_servings", nullable = false)
    var baseServings: Int = 1,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var createBy: User? = null,

    @ManyToMany
    @JoinTable(
        name = "recipe_category",
        joinColumns = [JoinColumn(name = "recipe_id")],
        inverseJoinColumns = [JoinColumn(name = "category_value_id")]
    )
    var categories: MutableSet<CategoryValue> = mutableSetOf(),
//    @ManyToMany
//    @JoinTable(
//        name = "recipe_category",
//        joinColumns = [JoinColumn(name = "recipe_id")],
//        inverseJoinColumns = [JoinColumn(name = "category_id")]
//    )
//    var categories: MutableSet<Category> = mutableSetOf(),

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
