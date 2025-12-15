package com.grig.restapirecipes.model

import jakarta.persistence.*

@Entity
@Table(name = "recipe")
data class Recipe(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

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

    @ManyToMany
    @JoinTable(
        name = "recipe_ingredient",
        joinColumns = [JoinColumn(name = "recipe_id")],
        inverseJoinColumns = [JoinColumn(name = "ingredient_id")]
    )
    var ingredients: MutableSet<Ingredient> = mutableSetOf(),

    @OneToMany(
        mappedBy = "recipe",
        cascade = [CascadeType.ALL],
        orphanRemoval = true
    )
    @OrderBy("stepNumber ASC")
    var steps: MutableList<CookingStep> = mutableListOf(),

    @OneToMany(mappedBy = "recipe", cascade = [CascadeType.ALL], orphanRemoval = true)
    val recipeIngredients: MutableSet<RecipeIngredient> = mutableSetOf()
)
