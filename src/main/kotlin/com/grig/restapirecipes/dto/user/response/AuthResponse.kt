package com.grig.restapirecipes.dto.user.response

data class AuthResponse(
    val token: String,
    val username: String,
    val email: String
)
