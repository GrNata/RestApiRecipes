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

    @Column(name = "name_eng")
    var nameEng: String? = null,

    @Column(name = "energy_kcal_100g")
    var energyKcal100g: Int? = null,

) {
    constructor(
        name: String,
        nameEng: String?,
        energyKcal100g: Int?
    ): this(null, name, nameEng, energyKcal100g) {
        this.name = name
        this.nameEng = nameEng
        this.energyKcal100g = energyKcal100g
    }
//    {
//    constructor(name: String, unit: String): this(null, name, unit) {
//        this.name = name
//        this.unit = unit
//    }
}
