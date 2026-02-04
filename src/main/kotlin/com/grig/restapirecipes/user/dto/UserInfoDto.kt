package com.grig.restapirecipes.user.dto

data class UserInfoDto(
    val email: String,
    val roles: Set<String>
)
