package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.user.request.LoginRequest
import com.grig.restapirecipes.dto.user.request.RegisterRequest
import com.grig.restapirecipes.dto.user.response.AuthResponse
import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.security.JwtTokenProvider
import com.grig.restapirecipes.user.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun register(request: RegisterRequest) : AuthResponse {
        if (userRepository.existsByEmail(request.email)) {
            throw IllegalArgumentException("Email alredy in use")
        }
        val user = User(
            username = request.username,
            email = request.email,
            password = passwordEncoder.encode(request.password)
        )
        userRepository.save(user)

        val token = jwtTokenProvider.generateToken(user.email)
        return AuthResponse(token, user.username, user.email)
    }

    fun login(request: LoginRequest) : AuthResponse {
        val user = userRepository.findByEmail(request.email)
            .orElseThrow { IllegalArgumentException("Invalid email or password") }
        val token = jwtTokenProvider.generateToken(user.email)
        return AuthResponse(token, user.username, user.email)
    }
}