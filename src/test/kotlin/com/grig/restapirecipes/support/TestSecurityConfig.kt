package com.grig.restapirecipes.support

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@EnableMethodSecurity
class TestSecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .httpBasic {  }  // только базовая авторизация для тестов
        return http.build()
    }

//    👉 НЕТ JwtAuthenticationFilter
//👉 НЕТ JwtTokenProvider
//👉 НЕТ CustomUserDetailsService

}