package com.grig.restapirecipes.security

import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.java


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig (
    private val customUserDetailsService: CustomUserDetailsService,
    private val jwtTokenProvider: JwtTokenProvider,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter
) {
    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationManager(config: AuthenticationConfiguration) : AuthenticationManager =
        config.authenticationManager


//    @Bean
//    fun filterChain(
//        http: HttpSecurity,
//        jwtAuthFilter: JwtAuthenticationFilter
//    ): SecurityFilterChain {
//
//        http
//            .csrf { it.disable() }
//            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
//            .authorizeHttpRequests { auth ->
//                auth.requestMatchers(
//                    "/api/auth/**",
//                    "/v3/api-docs/**",
//                    "/swagger-ui/**",
////                    "/swagger-ui.html"
//                ).permitAll()
//                auth.requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll()
//                auth.anyRequest().authenticated()
//            }
//            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
//
//        return http.build()
//    }



//    JWT-фильтр добавляй ТОЛЬКО если активен профиль prod в JwtAuthenticationFilter

////    Оставляем БЕЗ JWT для unit-тестов:
//    @Bean
//    fun filterChain(http: HttpSecurity): SecurityFilterChain {
//        http
//            .csrf { it.disable() }
//////            для консоли H2
////            .headers { it.frameOptions { it.sameOrigin() } }
////            .authorizeHttpRequests { auth ->
////                auth.requestMatchers("/h2-console/**").permitAll()
////                    .anyRequest().authenticated()
////            }
//            .authorizeHttpRequests {
//                it.anyRequest().authenticated()
//            }
//            .httpBasic {}
//
//        return http.build()
//    }

//     которая объединяет все нужные правила
    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .headers { it.frameOptions { it.sameOrigin() } }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { auth ->
                auth
                    .requestMatchers("/h2-console/**").permitAll()
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/v3/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/recipes/**").permitAll() // явно GET
//                    .requestMatchers(HttpMethod.GET, "/api/favorites").permitAll()   // <--- добавляем сюда
                    .anyRequest().authenticated()
            }
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter::class.java
            )
            .httpBasic(withDefaults()) // для админ-панели / unit-тестов
//                auth.requestMatchers(
//                    "/h2-console/**",
//                    "/api/auth/**",
//                    "/v3/api-docs/**",
//                    "/swagger-ui/**",
//                    "/api/recipes/**"
//                ).permitAll()
//                    .anyRequest().authenticated()
//            }
        return http.build()
    }
}