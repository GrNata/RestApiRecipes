package com.grig.restapirecipes.repository

import com.grig.restapirecipes.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String) : Optional<User>

    fun existsByEmail(email: String): Boolean
}