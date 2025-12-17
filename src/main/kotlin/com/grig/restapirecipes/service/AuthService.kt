package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.user.request.LoginRequest
import com.grig.restapirecipes.dto.user.request.RegisterRequest
import com.grig.restapirecipes.dto.user.response.AuthResponse
import com.grig.restapirecipes.repository.RoleRepository
import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.security.JwtTokenProvider
import com.grig.restapirecipes.user.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val roleRepository: RoleRepository
) {

    fun register(request: RegisterRequest) : AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email already in use")
        }

        val roleUser = roleRepository.findByName("ROLE_USER")
            .orElseThrow { IllegalArgumentException("ROLE_USER not found.") }

        val encodedPassword: String = passwordEncoder.encode(request.password)
            ?: throw java.lang.IllegalArgumentException("Password encoding faild")

        val user = User(
            username = request.username,
            email = request.email,
            password = encodedPassword,
            roles = mutableSetOf(roleUser)
        )
        userRepository.save(user)

//        val token = jwtTokenProvider.generateToken(user.email)
        val token = jwtTokenProvider.generateToken(user)

        return AuthResponse(
            token,
            user.username,
            user.email)
    }

    fun login(request: LoginRequest) : AuthResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Invalid email or password") }

        if (!passwordEncoder.matches(request.password, user.password)) {
            throw  IllegalArgumentException("Invalid email or password.")
        }
//        val token = jwtTokenProvider.generateToken(user.email)
        val token = jwtTokenProvider.generateToken(user)

        return AuthResponse(
            token,
            user.username,
            user.email)
    }
}