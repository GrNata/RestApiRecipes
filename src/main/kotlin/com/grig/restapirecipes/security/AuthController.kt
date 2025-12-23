package com.grig.restapirecipes.security

import com.grig.restapirecipes.dto.RefreshTokenRequest
import com.grig.restapirecipes.dto.TokenResponse
import com.grig.restapirecipes.dto.user.request.LoginRequest
import com.grig.restapirecipes.dto.user.request.RegisterRequest
import com.grig.restapirecipes.dto.user.response.AuthResponse
import com.grig.restapirecipes.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Регистрация, логин и обновление токена")
class AuthController(private val authService: AuthService) {

    @Operation(summary = "Регистрация нового пользователя")
    @PostMapping("/register")
    fun register(@Valid @RequestBody request: RegisterRequest) : ResponseEntity<AuthResponse> {
        val response = authService.register(request)
        return ResponseEntity.ok(response)
    }

    @Operation(summary = "Логин пользователя")
    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequest) : ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.login(request.email, request.password))
    }

    @Operation(summary = "refresh-token")
    @PostMapping("/refresh-token")
    fun refreshToken(@RequestBody request: RefreshTokenRequest) : ResponseEntity<TokenResponse> {
        return ResponseEntity.ok(authService.refreshToken(request))
    }
}

//	•	LoginRequest → содержит email и пароль.
//	•	RefreshTokenRequest → содержит refresh token.
//	•	AuthResponse → содержит accessToken и refreshToken.

//4️⃣ Примечания
//	•	Access Token: короткий срок жизни (15 мин), используется для всех защищённых эндпоинтов.
//	•	Refresh Token: длинный срок жизни (30 дней), хранится в базе, используется только для обновления Access Token.
//	•	JWT подписывается секретом из application.yml.