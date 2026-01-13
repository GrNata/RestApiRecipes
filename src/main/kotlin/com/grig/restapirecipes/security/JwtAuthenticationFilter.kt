package com.grig.restapirecipes.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Profile
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

//@Profile("prod")        // Оставляем БЕЗ JWT для unit-тестов:
@Component
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userDetailsService: CustomUserDetailsService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        println("JwtAuthenticationFilter - start")

        val authHeader = request.getHeader("Authorization")
        val token = authHeader
            ?.takeIf { it.startsWith("Bearer ") }
            ?.substring(7)

        println("JwtAuthenticationFilter получен - token=$token")

        if (token.isNullOrEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        val email = jwtTokenProvider.getEmailFromJWT(token)
        println("JwtAuthenticationFilter - email = $email")

        //  защита от пустого subject
        if (email.isNullOrBlank()) {
            filterChain.doFilter(request, response)
            return
        }

        //  не затираем уже установленную аутентификацию
        if (SecurityContextHolder.getContext().authentication == null) {
            val userDetails = userDetailsService.loadUserByUsername(email)

            val authentication = UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.authorities
            )

//        if (!token.isNullOrEmpty() && jwtTokenProvider.validateToken(token)) {
//            println("JwtAuthenticationFilter - token not NULL")
//
//            val email = jwtTokenProvider.getEmailFromJWT(token)
//
//            println("JwtAuthenticationFilter - email = $email")
//
//            val userDetails = userDetailsService.loadUserByUsername(email)
//
//            val authentication = UsernamePasswordAuthenticationToken(
//                userDetails, null, userDetails.authorities
//            )

            println("authorities = ${userDetails.authorities}")

            authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
            SecurityContextHolder.getContext().authentication = authentication
        }
        filterChain.doFilter(request, response)
    }
}