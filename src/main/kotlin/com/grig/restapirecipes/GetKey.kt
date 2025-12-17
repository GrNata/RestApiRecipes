//package com.grig.restapirecipes
//
//import io.jsonwebtoken.SignatureAlgorithm
//import io.jsonwebtoken.security.Keys
//import java.util.Base64
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
//
//// рабочий
//fun main() {
//    val passwordEncoder = BCryptPasswordEncoder()
//    val adminPassword = passwordEncoder.encode("admin1971")
//    val userPassword = passwordEncoder.encode("user1971")
//
//    println("Admin hashed password: $adminPassword")
//    println("User hashed password: $userPassword")
//}





//fun main() {
//    val key = Keys.secretKeyFor(SignatureAlgorithm.HS512)
//    val encoded = Base64.getEncoder().encodeToString(key.encoded)
//    println(encoded)
//}


//fun main() {
//    val password = "user1971"
//    val passwordEncoder = org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder()
//    val encodedPassword = passwordEncoder.encode(password)
//    println(encodedPassword)
//}