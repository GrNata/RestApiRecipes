package com.grig.restapirecipes.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.grig.restapirecipes.dto.RefreshTokenRequest
import com.grig.restapirecipes.dto.TokenResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objestMapper: ObjectMapper

    @MockBean
    lateinit var authService: AuthService

    @Test
    fun `login returns 200 with tokens`() {
        val loginRequest = mapOf("email" to "user@mail.ru", "password" to "wrong")

        Mockito.`when`(authService.login("user@mail.ru", "wrong"))
            .thenThrow(IllegalArgumentException("Invalid password"))

        mockMvc.perform(
            post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objestMapper.writeValueAsString(loginRequest))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Invalid password"))
    }

    @Test
    fun `refresh token returns new token`() {
        val request = RefreshTokenRequest("refresh")
        val response = TokenResponse("newAccess", "newRefresh")

        Mockito.`when`(authService.refreshToken(request)).thenReturn(response)

        mockMvc.perform(
            post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objestMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("newAccess"))
            .andExpect(jsonPath("$.refreshToken").value("newRefresh"))
    }

    @Test
    fun `refresh token returns 400 for invalid token`() {
        val request = RefreshTokenRequest("invalid")

        Mockito.`when`(authService.refreshToken(request))
            .thenThrow(IllegalArgumentException("Invalid refresh token"))

        mockMvc.perform(
            post("/api/auth/refresh-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objestMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.message").value("Invalid refresh token"))
    }

}