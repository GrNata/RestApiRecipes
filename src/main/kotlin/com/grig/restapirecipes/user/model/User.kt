package com.grig.restapirecipes.user.model

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import org.hibernate.sql.results.graph.Fetch
import java.time.LocalDateTime

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
    var password: String,

    @Column(name = "registration_date")
    val registrationDate: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var blocked: Boolean = false,

    @ManyToMany
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    val roles: MutableSet<Role> = mutableSetOf()

)
{
    /** ✔ удобный конструктор для register */
    constructor(username: String, email: String, password: String, roles: MutableSet<Role>) : this(
        id = null,
        username = username,
        email = email,
        password = password,
        roles = roles
    )
}