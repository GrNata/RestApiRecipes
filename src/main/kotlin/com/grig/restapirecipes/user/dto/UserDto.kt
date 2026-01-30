package com.grig.restapirecipes.user.dto

import java.time.LocalDateTime

data class UserDto(
    val id: Long?,
    val username: String,
    val email: String,
    val registrationDate: LocalDateTime,
    val roles: Set<String>,
    val blocked: Boolean
)
