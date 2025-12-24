package com.grig.restapirecipes.dto

data class ChangePasswordRequest(
    val oldPassword: String,
    val newPassword: String
)
