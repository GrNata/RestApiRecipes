package com.grig.restapirecipes.model

import jakarta.persistence.*

@Entity
@Table(name = "category")
class Category(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    var name: String,

    @Column
    var image: String? = null
)
