package com.grig.restapirecipes.model

import jakarta.persistence.*
import java.time.Instant


//Access Token – короткоживущий JWT (например, 15 минут), используется для авторизации API-запросов.
//	•	Refresh Token – долгоживущий JWT или UUID (например, 7–30 дней), используется для получения нового access токена без повторного логина.
//	•	Access токен хранится в клиенте (например, Android), refresh токен – тоже в клиенте, но безопаснее в SharedPreferences с шифрованием.
@Entity
@Table(name = "refresh_token")
class RefreshToken(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var token: String,
    val userEmail: String,
    var expiryDate: Instant
)

//  •	Таблица нужна, если мы хотим отозвать токен или контролировать срок жизни.
//	•	Если не нужна БД – можно генерировать JWT refresh токен без хранения.
