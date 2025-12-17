package com.grig.restapirecipes.repository

import com.grig.restapirecipes.user.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String) : Optional<Role>
}