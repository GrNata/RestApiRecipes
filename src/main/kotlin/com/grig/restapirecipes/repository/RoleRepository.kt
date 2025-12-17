package com.grig.restapirecipes.repository

import com.grig.restapirecipes.user.model.Role
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface RoleRepository : JpaRepository<Role, Long> {

    fun findByName(name: String) : Optional<Role>
}