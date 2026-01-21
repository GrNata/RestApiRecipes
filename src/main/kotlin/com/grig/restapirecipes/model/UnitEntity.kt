package com.grig.restapirecipes.model

import jakarta.persistence.*

@Entity
@Table(name = "unit")
data class UnitEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    val id: Long = 0,
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var code: String,   //      G, KG, ML, PCS

    @Column(nullable = false)
    var label: String   //  г, кг, мл, шт
) {
    constructor(code: String, label: String): this(null, code, label)
}
