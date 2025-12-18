package com.grig.restapirecipes.security

import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.user.model.User
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(email: String): UserDetails {
//        val user: User = userRepository.findByEmail(email)
        val user: User = userRepository.findByEmailWithRoles(email)
            ?: throw UsernameNotFoundException("User not found with email: ${email}")

        val authorities = user.roles.map {
            SimpleGrantedAuthority(it.name)     // ROLE_USER / ROLE_ADMIN
        }

        return org.springframework.security.core.userdetails.User(
            user.email,
            user.password,
//            emptyList()
            authorities
        )
    }
}