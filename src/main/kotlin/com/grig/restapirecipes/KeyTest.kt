package com.grig.restapirecipes

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import java.util.Base64

fun main() {
    val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
    val encoded = Base64.getEncoder().encodeToString(key.encoded)
    println(encoded)
}