package com.grig.restapirecipes.service

import com.grig.restapirecipes.dto.user.request.LoginRequest
import com.grig.restapirecipes.dto.user.request.RegisterRequest
import com.grig.restapirecipes.repository.RoleRepository
import com.grig.restapirecipes.repository.UserRepository
import com.grig.restapirecipes.security.JwtTokenProvider
import com.grig.restapirecipes.user.model.Role
import com.grig.restapirecipes.user.model.User
//import org.hamcrest.CoreMatchers.any
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
//import org.mockito.kotlin.any
//import org.mockito.kotlin.times
//import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.security.SecurityProperties
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import java.util.Optional

import org.mockito.kotlin.any
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.times
import kotlin.collections.mutableSetOf


//нужно тестировать регистрацию и логин, включая корректные данные, дубликаты и неверные пароли.
// Основные подходы: использовать @SpringBootTest с H2, @MockBean для репозиториев и JUnit 5 + assertThrows/assertDoesNotThrow.
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    lateinit var authService: AuthService

    @MockBean
    lateinit var userRepository: UserRepository

    @MockBean
    lateinit var roleRepository: RoleRepository

    @MockBean
    lateinit var passwordEncoder: PasswordEncoder

    @MockBean
    lateinit var jwtTokenProvider: JwtTokenProvider

//    Registretion
    @Test
    fun `register success`() {
        val request = RegisterRequest("user1", "user1@mail.com", "password")

        val roleUser = Role(id = 1, name = "ROLE_USER")
        `when`(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser))
        `when`(userRepository.existsByEmail(request.email)).thenReturn(false)
        `when`(passwordEncoder.encode(request.password)).thenReturn("encodePassword")
        `when`(jwtTokenProvider.generateToken(any<User>())).thenReturn("token123")

        val response = authService.register(request)

        assertEquals("token123", response.token)
        assertEquals(request.username, response.username)
        assertEquals(request.email, response.email)

        verify(userRepository, times(1)).save(any<User>())
    }

    @Test
    fun `login success`() {
        val request = LoginRequest("user1@mail.com", "password")
        val user = User("user1", "user1@mail.com", "encodePassword", mutableSetOf())

        `when`(userRepository.findByEmail(request.email)).thenReturn(user)
        `when`(passwordEncoder.matches(request.password, user.password)).thenReturn(true)
        `when`(jwtTokenProvider.generateToken(user)).thenReturn("token123")

        val response = authService.login(request)

        assertEquals("token123", response.token)
        assertEquals(user.username, response.username)
        assertEquals(user.email, response.email)
    }

//    Логин с неверным паролем
    @Test
    fun `login fails with wrong password`() {
        val request = LoginRequest("user1@mail.com", "wrongPassword")
        val user = User("user1", "user1@mail.com", "encodedPassword", mutableSetOf())

        `when`(userRepository.findByEmail(request.email)).thenReturn(user)
        `when`(passwordEncoder.matches(request.password, user.password)).thenReturn(false)

        val ex = assertThrows<IllegalArgumentException> {
            authService.login(request)
        }

        assertEquals("Invalid email or password.", ex.message)
    }

//    Логин с несуществующим email
    @Test
    fun `login fail with wrong email`() {
        val request = LoginRequest("wrong@mail.com", "password")
        val user = User("user1", "user1@mail.com", "encodePassword", mutableSetOf())

        `when`(userRepository.findByEmail(request.email)).thenReturn(user)
        `when`(passwordEncoder.matches(request.password, user.password)).thenReturn(false)

        val ex = assertThrows<IllegalArgumentException> {
            authService.login(request)
        }

        assertEquals("Invalid email or password.", ex.message)
    }
//Роль ROLE_USER не найдена при регистрации.
    @Test
    fun `register fails if role USER not found`() {
        val request = RegisterRequest("user1", "user1@mail.com", "password")

        // Роль не возвращается
        `when`(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty())

        val ex = assertThrows<IllegalArgumentException> {
            authService.register(request)
        }

        assertEquals("ROLE_USER not found.", ex.message)
    }


//	•	Проверка корректного формирования AuthResponse.
    @Test
    fun `register returns correct AuthResponse`() {
        val request = RegisterRequest("user1", "user1@mail.com", "password")
        val roleUser = Role(id = 1, name = "ROLE_USER")

        `when`(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(roleUser))
        `when`(userRepository.existsByEmail(request.email)).thenReturn(false)
        `when`(passwordEncoder.encode(request.password)).thenReturn("encodedPassword")
        `when`(jwtTokenProvider.generateToken(any<User>())).thenReturn("token123")

        val response = authService.register(request)

        // Проверяем все поля AuthResponse
        assertEquals(request.username, response.username)
        assertEquals(request.email, response.email)
        assertEquals("token123", response.token)
    }

//    проверка исключений
    @Test
    fun `Register new user with email  already in use`() {
        assertThrows<IllegalArgumentException> { authService.register(RegisterRequest("user2", "user@mail.ru", "password")) }
    }

    @Test
    fun `Login invalid email`() {
        assertThrows<IllegalArgumentException> { authService.login(LoginRequest( "user99@mail.ru", "user1971")) }
    }

    @Test
    fun `Login invalid password`() {
        assertThrows<IllegalArgumentException> { authService.login(LoginRequest( "admin@mail.ru", "wrongPassword")) }
    }
}