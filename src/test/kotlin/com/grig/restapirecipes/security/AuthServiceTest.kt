package com.grig.restapirecipes.security

import com.grig.restapirecipes.dto.RefreshTokenRequest
import com.grig.restapirecipes.model.RefreshToken
import com.grig.restapirecipes.repository.RefreshTokenRepository
import com.grig.restapirecipes.repository.RoleRepository
import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.user.model.User
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import kotlin.test.assertEquals

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {
    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @MockK
    lateinit var jwtTokenProvider: JwtTokenProvider

    @MockK
    lateinit var passwordEncoder: PasswordEncoder

    @MockK
    lateinit var roleRepository: RoleRepository

    lateinit var authService: AuthService

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxed = true)  // важно для инициализации всех @MockK
        authService = AuthService(userRepository, passwordEncoder, jwtTokenProvider, roleRepository, refreshTokenRepository)
    }

    @Test
    fun `login returns tokens for valid user`() {
        val user = User(1L, "user@mail.ru", "user", "encoded")
        every { userRepository.findByEmail("user@mail.ru") } returns user
        every { passwordEncoder.matches("pass", "encoded") } returns true
        every { jwtTokenProvider.generateAccessToken(user) } returns "access"
        every { jwtTokenProvider.generateRefreshToken(user) } returns RefreshToken(
            token = "refresh",
            userEmail = user.email,
            expiryDate = Instant.now().plusSeconds(3600)
        )

        every { refreshTokenRepository.save(any()) } answers { firstArg<RefreshToken>() }

        val response = authService.login("user@mail.ru", "pass")

        assertEquals("access", response.accessToken)
        assertEquals("refresh", response.refreshToken)
        verify { refreshTokenRepository.save(any()) }

    }

    @Test
    fun `login throws exception for unknown email`() {
        every { userRepository.findByEmail("user@mail.ru") } returns null

        val ex = assertThrows<IllegalArgumentException> {
            authService.login("user@mail.ru", "pass")
        }

        assertEquals("User not found", ex.message)
    }

    @Test
    fun `login throws exception for invalid password`() {
        val user = User(id = 1, email = "user@mail.ru", username = "user", password = "encoded")
        every { userRepository.findByEmail("user@mail.ru") } returns user
        every { passwordEncoder.matches("wrong", "encoded") } returns false

        val ex = assertThrows<IllegalArgumentException> {
            authService.login("user@mail.ru", "wrong")
        }

        assertEquals("Invalid password", ex.message)
    }

    @Test
    fun `refreshToken returns new tokens for valid token`() {
        val storedToken = RefreshToken(id = 1, token = "refresh", userEmail = "user@mail.ru", expiryDate = Instant.now().plusSeconds(3600))
        val user = User(id = 1, email = "user@mail.ru", username = "user", password = "encoded")
        every { refreshTokenRepository.findByToken("refresh") } returns storedToken
        every { userRepository.findByEmail("user@mail.ru") } returns user
        every { jwtTokenProvider.generateAccessToken(user) } returns "newAccess"
        every { jwtTokenProvider.generateRefreshToken(user) } returns RefreshToken(token = "newRefresh", userEmail = user.email, expiryDate = Instant.now().plusSeconds(3600))
        every { refreshTokenRepository.save(any()) } answers { firstArg<RefreshToken>() }

        val response = authService.refreshToken(RefreshTokenRequest("refresh"))

        assertEquals("newAccess", response.accessToken)
        assertEquals("newRefresh", response.refreshToken)
        verify { refreshTokenRepository.save(storedToken) }
    }

    @Test
    fun `refreshToken throws exception for expired token`() {
        val storedToken = RefreshToken(id = 1, token = "refresh", userEmail = "user@mail.ru", expiryDate = Instant.now().minusSeconds(10))
        every { refreshTokenRepository.findByToken("refresh") } returns storedToken

        val ex = assertThrows<IllegalArgumentException> {
            authService.refreshToken(RefreshTokenRequest("refresh"))
        }
        assertEquals("Refresh token expired", ex.message)
    }

}