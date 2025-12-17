package com.grig.restapirecipes.auth

import com.grig.restapirecipes.dto.user.request.LoginRequest
import com.grig.restapirecipes.dto.user.request.RegisterRequest
import com.grig.restapirecipes.dto.user.response.AuthResponse
import com.grig.restapirecipes.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity


@RestController
@RequestMapping("api/auth")
class AuthController(
    private val authService: AuthService
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest) : ResponseEntity<AuthResponse> {
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }

    @PostMapping("/login")
    fun login(@Valid @RequestBody request: LoginRequest) : ResponseEntity<AuthResponse> {
        val response = authService.login(request)
        return ResponseEntity.ok(response)
    }
}