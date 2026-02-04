package com.grig.restapirecipes.dto

import com.grig.restapirecipes.user.dto.UserInfoDto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String,
    val userInfo: UserInfoDto
)
//data class TokenResponse(
//    val accessToken: String,
//    val refreshToken: String
//)

data class RefreshTokenRequest(
    val refreshToken: String
)
