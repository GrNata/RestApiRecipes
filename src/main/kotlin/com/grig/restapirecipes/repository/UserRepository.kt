package com.grig.restapirecipes.repository

import com.grig.restapirecipes.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String) : User?

    fun existsByEmail(email: String): Boolean

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.roles
        WHERE u.email = :email
    """)
    fun findByEmailWithRoles(email: String): User?
}