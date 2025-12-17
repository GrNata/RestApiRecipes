package com.grig.restapirecipes.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var username: String,

    @Column(nullable = false, unique = true)
    @Email
    var email: String,

    @Column(nullable = false)
    var password: String?

)