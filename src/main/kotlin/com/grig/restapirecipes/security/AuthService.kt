package com.grig.restapirecipes.security

import com.grig.restapirecipes.dto.RefreshTokenRequest
import com.grig.restapirecipes.dto.TokenResponse
import com.grig.restapirecipes.dto.user.request.RegisterRequest
import com.grig.restapirecipes.dto.user.response.AuthResponse
import com.grig.restapirecipes.repository.RefreshTokenRepository
import com.grig.restapirecipes.repository.RoleRepository
import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.user.model.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.time.temporal.ChronoUnit

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val roleRepository: RoleRepository,
    private val refreshTokenRepository: RefreshTokenRepository
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
            user.email
        )
    }

//    fun login(request: LoginRequest) : AuthResponse {
//        val user = userRepository.findByEmail(request.email)
//            ?: throw  IllegalArgumentException("Invalid email or password.")
////            .orElseThrow { IllegalArgumentException("Invalid email or password") }
//
//        if (!passwordEncoder.matches(request.password, user.password)) {
//            throw  IllegalArgumentException("Invalid email or password.")
//        }
////        val token = jwtTokenProvider.generateToken(user.email)
//        val token = jwtTokenProvider.generateToken(user)
//
//        return AuthResponse(
//            token,
//            user.username,
//            user.email)
//    }

    fun login(email: String, password: String): TokenResponse {
        val user = userRepository.findByEmail(email)
            ?: throw IllegalArgumentException("User not found")

        if (!checkPassword(password, user.password)) {
            throw IllegalArgumentException("Invalid password")
        }

        // Генерация токенов
        val accessToken = jwtTokenProvider.generateAccessToken(user)
        val refreshToken = jwtTokenProvider.generateRefreshToken(user)

        // Сохраняем refresh token в БД
        refreshTokenRepository.save(refreshToken)

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken.token

        )
    }

//    Использование Refresh Token
//  •	Когда Access Token истёк, клиент присылает refresh token.
//	•	На сервере проверяем, существует ли он и не истёк ли:
    fun refreshToken(request: RefreshTokenRequest): TokenResponse {
        val storedToken = refreshTokenRepository.findByToken(request.refreshToken)
            ?: throw IllegalArgumentException("Invalid refresh token")

        if (storedToken.expiryDate.isBefore(Instant.now())) {
            throw IllegalArgumentException("Refresh token expired")
        }

        val user = userRepository.findByEmail(storedToken.userEmail)
            ?: throw IllegalArgumentException("User not found")

        val newAccessToken = jwtTokenProvider.generateAccessToken(user)
        val newRefreshToken = jwtTokenProvider.generateRefreshToken(user)

        // обновляем refresh token в базе
        storedToken.token = newRefreshToken.token
        storedToken.expiryDate = Instant.now().plus(30, ChronoUnit.DAYS)
        refreshTokenRepository.save(storedToken)

        return TokenResponse(
            newAccessToken,
            newRefreshToken.token
        )
    }

    @Transactional
    fun logout(refreshToken: String) {
        refreshTokenRepository.deleteByToken(refreshToken)
    }

    @Transactional
    fun logoutAll(userEmail: String) {
        refreshTokenRepository.deleteAllByUserEmail(userEmail)
    }


    private fun checkPassword(rawPassword: String, encodedPassword: String): Boolean {
        return passwordEncoder.matches(rawPassword, encodedPassword)
    }
}