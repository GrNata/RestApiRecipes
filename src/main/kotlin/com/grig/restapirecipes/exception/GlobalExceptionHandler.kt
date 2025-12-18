package com.grig.restapirecipes.exception

//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.RestControllerAdvice
////import org.springframework.validation.FieldError
////import org.springframework.web.bind.MethodArgumentNotValidException
////import org.springframework.web.bind.MethodArgumentNotValidException
//import jakarta.validation.ConstraintViolationException
////import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
////import org.springframework.validation.BindException

//import jakarta.validation.ConstraintViolationException
//import org.springframework.http.HttpStatus
//import org.springframework.http.ResponseEntity
//import org.springframework.validation.FieldError
//import org.springframework.web.bind.MethodArgumentNotValidException
//import org.springframework.web.bind.annotation.ExceptionHandler
//import org.springframework.web.bind.annotation.RestControllerAdvice
//import java.time.LocalDateTime
//import org.springframework.security.authorization.AuthorizationDeniedException
//import org.springframework.security.core.AuthenticationException
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authorization.AuthorizationDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime
//import javax.validation.ConstraintViolationException


//  Глобальный обработчик исключений в Spring Boot обычно создают с помощью аннотации:
//  перехватывает исключения со всех контроллеров и возвращает стабильный JSON с кодом ошибки и сообщением.


@RestControllerAdvice
class GlobalExceptionHandler {

    // 404 Not Found
    @ExceptionHandler(RecipeNotFoundException::class)
    fun handlerNotFound(ex: RecipeNotFoundException): ResponseEntity<Map<String, Any?>> {
        val body = mapOf(
            "status" to 404,
            "error" to "Not Found",
            "message" to (ex.message ?: "Resource not found")
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }

    // 400 - Валидация @Valid в контроллерах
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any?>> {
        val errors = ex.bindingResult.allErrors.map { error ->
            val field = (error as? FieldError)?.field ?: error.objectName
            val message = error.defaultMessage ?: "Invalid value"
            "$field: $message"
        }
        val body = mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to 400,
            "error" to "Validation Error",
            "message" to errors
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 400 - Валидация @Validated на сервисах
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Map<String, Any?>> {
        val errors = ex.constraintViolations.map { violation ->
            val field = violation.propertyPath.toString()
            val message = violation.message
            "$field: $message"
        }
        val body = mapOf(
            "timestamp" to LocalDateTime.now(),
            "status" to 400,
            "error" to "Validation Error",
            "message" to errors
        )
        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
    }

    // 401 Unauthorized для неавторизованных
    @ExceptionHandler(AuthenticationException::class)
    fun handleUnauthorized(ex: AuthenticationException): ResponseEntity<Map<String, String>> {
        val body = mapOf(
            "error" to "Unauthorized",
            "message" to (ex.message ?: "No token or invalid token")
        )
        return ResponseEntity(body, HttpStatus.UNAUTHORIZED)
    }

    // 403 Forbidden для запрещенных действий
    @ExceptionHandler(value = [AccessDeniedException::class, AuthorizationDeniedException::class])
    fun handleForbidden(ex: Exception): ResponseEntity<Map<String, String>> {
        val body = mapOf<String, String>(
            "error" to "Access denied",
            "message" to (ex.message ?: "No message")
        )
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body)
    }

    // 400 Bad Request для IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        val body = mapOf<String, String>(
            "error" to "Bad request",
            "message" to (ex.message ?: "No message")
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
    }

    // 500 Internal Server Error для всех остальных
    @ExceptionHandler(Exception::class)
    fun handlerInternal(ex: Exception): ResponseEntity<Map<String, Any?>> {
        ex.printStackTrace() // логируем
        val body = mapOf(
            "status" to 500,
            "error" to "Internal Server Error",
            "message" to (ex.message ?: "Unexpected error")
        )
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }

//    // 404 Not Found
//    @ExceptionHandler(RecipeNotFoundException::class)
//    fun handlerNotFound(ex: RecipeNotFoundException) : ResponseEntity<Map<String, Any?>> {
//        val body = mapOf(
//            "status" to 404,
//            "error" to "Not Found",
//            "message" to ex.message
//        )
//        return ResponseEntity(body, HttpStatus.NOT_FOUND)
//    }
//
//    // 400 - Валидация для @Valid в контроллерах
//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException): ResponseEntity<Map<String, Any?>> {
//        val errors = ex.bindingResult.allErrors.map { error ->
//            val field = (error as? FieldError)?.field ?: error.objectName
//            val message = error.defaultMessage ?: "Invalid value"
//            "$field: $message"
//        }
//        val body = mapOf(
//            "timestamp" to LocalDateTime.now(),
//            "status" to 400,
//            "error" to "Validation Error",
//            "message" to errors
//        )
//        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
//    }
//
//    // 400 - Валидация для @Validated на методах сервисов
//    @ExceptionHandler(ConstraintViolationException::class)
//    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Map<String, Any?>> {
//        val errors = ex.constraintViolations.map { violation ->
//            val field = violation.propertyPath.toString()
//            val message = violation.message
//            "$field: $message"
//        }
//        val body = mapOf(
//            "timestamp" to LocalDateTime.now(),
//            "status" to 400,
//            "error" to "Validation Error",
//            "message" to errors
//        )
//        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
//    }
//
//
//
//    // 500 Internal Server Error для всех остальных исключений
//    @ExceptionHandler(Exception::class)
//    fun handlerInternal(ex: Exception) : ResponseEntity<Map<String, Any?>> {
//        val body = mapOf(
//            "status" to 500,
//            "error" to "Internal Server Error",
//            "message" to ex.message
//        )
//        ex.printStackTrace() // для логов
//        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
//    }
//
////    Для error 403 - при захождение USER куда не надо
//
////    Ловить все виды access denied
//    @ExceptionHandler(value = [AccessDeniedException::class, AuthorizationDeniedException::class])
//    fun handleForbidden(ex: Exception): ResponseEntity<Map<String, String>> {
//        val body = mapOf(
//            "error" to "Access denied",
//            "message" to ex.message ?: "No message"
//        )
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body)
//    }
//    @ExceptionHandler(AccessDeniedException::class)
//    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<Map<String, String>> {
//        val body: Map<String, String> = mapOf(
//            "error" to "Access denied",
//            "message" to (ex.message ?: "No message")
//        )
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body)
//    }
//
////    ловлю AuthenticationException для неавторизованных пользователей
//    @ExceptionHandler(AuthenticationException::class)
//    fun handleUnauthorized(ex: AuthenticationException): ResponseEntity<Map<String, String>> {
//        val body = mapOf(
//            "error" to "Unauthorized",
//            "message" to ex.message ?: "No token or invalid token"
//        )
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body)
//    }
//
//    @ExceptionHandler(IllegalArgumentException::class)
//    fun handleIllegalArgument(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
//        val body: Map<String, String> = mapOf(
//            "error" to "Bad request",
//            "message" to (ex.message ?: "No message")
//        )
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
//    }
//
//
//    // 400 Bad Request (в том числе валидация)
//    @ExceptionHandler(BadRequestException::class)
//    fun handlerBadRequest(ex: BadRequestException) : ResponseEntity<Map<String, Any?>> {
//        val body = mapOf(
//            "status" to 400,
//            "error" to "Bad Request",
//            "message" to ex.message
//        )
//        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
//    }
//
//    // 400 Bad Request при ошибках валидации @Valid
//    @ExceptionHandler(ConstraintViolationException::class)
//    fun handleConstraintViolation(ex: ConstraintViolationException): ResponseEntity<Map<String, Any?>> {
//        val errors = ex.constraintViolations.map { violation ->
//            val field = violation.propertyPath.toString()
//            val message = violation.message
//            "$field: $message"
//        }
//        val body = mapOf(
//            "status" to 400,
//            "error" to "Validation Error",
//            "message" to errors
//        )
//        return ResponseEntity(body, HttpStatus.BAD_REQUEST)
}