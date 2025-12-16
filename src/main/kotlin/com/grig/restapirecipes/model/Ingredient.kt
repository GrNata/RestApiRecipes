package com.grig.restapirecipes.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "ingredient")
class Ingredient(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var unit: String    //  кг, г, мл, шт, ст.л и т.д.
) {
    constructor(name: String, unit: String): this(null, name, unit) {
        this.name = name
        this.unit = unit
    }
}
