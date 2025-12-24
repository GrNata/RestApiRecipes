package com.grig.restapirecipes.model

import jakarta.persistence.*

@Entity
@Table(name = "unit")
data class UnitEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val code: String,   //      G, KG, ML, PCS

    @Column(nullable = false)
    val label: String   //  г, кг, мл, шт
)
