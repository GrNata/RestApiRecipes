package com.grig.restapirecipes.exception

import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

//  Глобальный обработчик исключений в Spring Boot обычно создают с помощью аннотации:
//  перехватывает исключения со всех контроллеров и возвращает стабильный JSON с кодом ошибки и сообщением.

@RestControllerAdvice
class GlobalExceptionHandler {

    // 404 Not Found
    @ExceptionHandler(RecipeNotFoundException::class)
    fun handleRecipeNotFound(
        ex: RecipeNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            ApiError(
                status = 404,
                error = "NOT_FOUND",
//                message = ex.message ?: "Recipe not found",
                message = ex.message,
                path = request.requestURI
            )
        )

    // 400 - Валидация @Valid DTO в контроллерах
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        request: HttpServletRequest
        ): ResponseEntity<ApiError> {

        val errors = ex.bindingResult.fieldErrors.associate {
            it.field to (it.defaultMessage ?: "Invalid value")
        }

        return ResponseEntity.badRequest().body(
            ApiError(
                status = 400,
                error = "VALIDATION_ERROR",
                message = errors,
                path = request.requestURI
            )
        )
    }

    // 400 - Валидация @Validated на сервисах
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(
        ex: ConstraintViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.badRequest().body(
            ApiError(
                status = 400,
                error = "VALIDATION_ERROR",
                message = ex.constraintViolations.map {
                    "${it.propertyPath}: ${it.message}"
                },
                path = request.requestURI
            )
        )

    // ========= 400 — IllegalArgumentException — ОСНОВА AuthService
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.badRequest().body(
            ApiError(
                status = 400,
                error = "BAD_REQUEST",
                message = ex.message,
                path = request.requestURI
            )
        )

    // ========= 409 — DB constraint =========
//    •	Пусть Hibernate / БД выбрасывает
//	•	Ты ловишь один раз глобально
//	•	Android получает 409 Conflict
//
//Типовые случаи:
//	•	слишком длинная строка
//	•	уникальный email
//	•	FK constraint
//	•	duplicate key
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(
        ex: DataIntegrityViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError(
                status = 409,
                error = "DATA_INTEGRITY_VIOLATION",
                message = "Operation violates database constraints",
                path = request.requestURI
            )
        )
//Hibernate (ОБЯЗАТЕЛЕН!) - Без метода handleHibernateConstraintViolation 409 не будет
    @ExceptionHandler(org.hibernate.exception.ConstraintViolationException::class)
    fun handleHibernateConstraintViolation(
        ex: org.hibernate.exception.ConstraintViolationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.CONFLICT).body(
            ApiError(
                status = 409,
                error = "DATA_INTEGRITY_VIOLATION",
                message = "Operation violates database constraints",
                path = request.requestURI
            )
        )

    // 401 Unauthorized для неавторизованных - Ошибки логина, JWT, refresh
    @ExceptionHandler(AuthenticationException::class)
    fun handleUnauthorized(
        ex: AuthenticationException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
            ApiError(
                status = 401,
                error = "UNAUTHORIZED",
                message = ex.message ?: "Authentication failed",
                path = request.requestURI
            )
        )

    // 403 Forbidden для запрещенных действий
    @ExceptionHandler(AccessDeniedException::class)
    fun handleForbidden(
        ex: AccessDeniedException,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> =
        ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            ApiError(
                status = 403,
                error = "FORBIDDEN",
                message = "Access denied",
                path = request.requestURI
            )
        )

    // 500 Internal Server Error для всех остальных
    @ExceptionHandler(Exception::class)
    fun handleInternal(
        ex: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ApiError> {
        ex.printStackTrace()
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
            ApiError(
                status = 500,
                error = "INTERNAL_SERVER_ERROR",
                message = "Unexpected error",
                path = request.requestURI
            )
        )
    }

//    для Auth добавь
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(
        ex: IllegalArgumentException,
        request: HttpServletRequest
    ) : ResponseEntity<ApiError> =
            ResponseEntity.badRequest().body(
                ApiError(
                    status = 400,
                    error = "BAD_REQUEST",
                    message = ex.message,
                    path = request.requestURI
                )
    )

}