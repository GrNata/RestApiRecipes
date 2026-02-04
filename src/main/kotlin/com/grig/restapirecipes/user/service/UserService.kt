package com.grig.restapirecipes.user.service

import com.grig.restapirecipes.repository.RoleRepository
import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.security.SecurityUtils.getCurrentUserEmail
import com.grig.restapirecipes.user.dto.UserDto
import com.grig.restapirecipes.user.model.Role
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val roleRepository: RoleRepository
) {
    @Transactional(readOnly = true)
    fun getAllUsers() : List<UserDto> {
        val users = userRepository.findAll().map { user ->
            UserDto(
                id = user.id,
                username = user.username,
                email = user.email,
                registrationDate = user.registrationDate,
                roles = user.roles.map { it.name }.toSet(),
                blocked = user.blocked
            )
        }

        println("ADMIN: users: $users")

        return users
    }

    @Transactional
    fun blockUser(userId: Long, blocked: Boolean) {
        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }
        user.blocked = blocked
    }

    @Transactional
    fun updateRoles(userId: Long, roleNames: Set<String>) {

        val currentAdminEmail = getCurrentUserEmail()
        val currentAdmin = userRepository.findByEmail(currentAdminEmail)
            ?: throw IllegalArgumentException("Current admin not found")

        val user = userRepository.findById(userId)
            .orElseThrow { IllegalArgumentException("User not found") }

        //        защита от “саморазжалования” админа
        if (user.id == currentAdmin.id && "ADMIN" !in roleNames) {
            throw IllegalArgumentException("Admin cannot remove ADMIN role from himself")
        }

        val roles = roleRepository.findAllByNameIn(roleNames)

        if (roles.size != roleNames.size) {
            throw IllegalArgumentException("One or more roles not found")
        }
        user.roles.clear()
        user.roles.addAll(roles)
    }


}