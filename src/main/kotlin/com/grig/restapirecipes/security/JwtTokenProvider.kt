package com.grig.restapirecipes.security

import com.grig.restapirecipes.model.RefreshToken
import com.grig.restapirecipes.user.model.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${jwt.secret}")
    private val jwtSecret: String,

    @Value("\${jwt.expiration}")
    private val jwtExpirationInMs: Long
//    @Value("\${jwt.secret}")
//    private val jwtSecret: String,
//
//    @Value("\${jwt.expiration}")
//    private val jwtExpirationInMs: Long
) {

    private lateinit var key: SecretKey

    @PostConstruct
    fun init() {
//        key = Keys.hmacShaKeyFor(jwtSecret.toByteArray())
        key = Keys.hmacShaKeyFor(
            Decoders.BASE64.decode(jwtSecret)
        )
    }

    fun generateToken(user: User): String {
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs)

        println("JWT SUBJECT = ${user.email}")

        return Jwts.builder()
//            .setSubject(authentication.name)
            .setSubject(user.email)
            .claim(
                "roles",
                user.roles.map { it.name }
            )
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key, SignatureAlgorithm.HS256)
            .compact()
    }
//    fun generateToken(user: User): String {
//        val roles = user.roles.map { it.name }
//
//        return Jwts.builder()
//            .setSubject(user.email)
//            .claim("roles", roles)
//            .setIssuedAt(Date())
//            .setExpiration(Date(System.currentTimeMillis() + jwtExpirationInMs))
//            .signWith(key, SignatureAlgorithm.HS512)
//            .compact()
//    }

    fun getEmailFromJWT(token: String): String =
        Jwts.parserBuilder().setSigningKey(key).build()
            .parseClaimsJws(token)
            .body.subject

//    //    Временно для отладки
//    fun validateToken(token: String): Boolean {
//        try {
//            val claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//            println("validateToken: success claims=${claims.body}")
//            return true
//        } catch (ex: Exception) {
//            println("validateToken: failed ex=$ex")
//            return false
//        }
//    }
    fun validateToken(token: String): Boolean =
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            true
        } catch (ex: Exception) {
            println("validateToken: failed ex=$ex")
            false
        }

//  •	subject — обычно ID пользователя.
//	•	claim — дополнительные данные, которые хочешь закодировать в JWT.
//	•	setExpiration — срок жизни Access Token короткий (15 минут).
    fun generateAccessToken(user: User): String {
        // срок жизни короткий, например 15 минут
        val now = Date()
    val expiryDate = Date(now.time + 15 * 60 * 1000)    //  15 минут

    return Jwts.builder()
//        .setSubject(user.id.toString())
        .setSubject(user.email)
        .claim("username", user.username)
        .claim("roles", user.roles.map { it.name })
        .setIssuedAt(now)
        .setExpiration(expiryDate)
        .signWith(SignatureAlgorithm.HS256, jwtSecret)
        .compact()
    }

    fun generateRefreshToken(user: User): RefreshToken {
        // срок жизни длинный, например 30 дней
        val token = UUID.randomUUID().toString()
        val expiryDate = LocalDateTime.now().plusDays(30).toInstant(ZoneOffset.UTC)   //  30 дней

        return RefreshToken(
            token = token,
            userEmail = user.email,
            expiryDate = expiryDate
        )
    }

}