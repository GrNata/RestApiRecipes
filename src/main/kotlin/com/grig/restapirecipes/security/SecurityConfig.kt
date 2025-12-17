package com.grig.restapirecipes.security

import org.apache.tomcat.util.net.openssl.ciphers.Authentication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain


@Configuration
class SecurityConfig(
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration) : AuthenticationManager =
        config.authenticationManager

    @Bean
    fun filterChain(http: HttpSecurity) : SecurityFilterChain {
        http
            .csrf { it.disable() }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests {
//                смотри пути: "api/auth/**", "/swagger-ui/**", "/v3/api-docs/**"  - ????
                it.requestMatchers("/api/auth/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                    it.anyRequest().authenticated()
            }
        return http.build()
    }
}