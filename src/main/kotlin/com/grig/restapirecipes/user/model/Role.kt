package com.grig.restapirecipes.user.model

import jakarta.persistence.*

@Entity
@Table(name = "roles")
class Role(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false, unique = true)
    val name: String

)