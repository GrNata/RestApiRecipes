package com.grig.restapirecipes.exception

import java.time.LocalDateTime

data class ApiError(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: Any?,
    val path: String,
)
