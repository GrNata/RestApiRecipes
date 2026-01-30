package com.grig.restapirecipes.security

import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtils {

    fun getCurrentUserEmail(): String {
        val authentication = SecurityContextHolder.getContext().authentication
            ?: throw IllegalArgumentException("No authentication found")

        return authentication.name
    }
    //  authentication.name возвращает:
//  return User(
//    user.email,
//    user.password,
//    authorities
// )

    fun getAuthentication() = SecurityContextHolder.getContext().authentication
}

