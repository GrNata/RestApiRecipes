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

import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime




//  Глобальный обработчик исключений в Spring Boot обычно создают с помощью аннотации:
//  перехватывает исключения со всех контроллеров и возвращает стабильный JSON с кодом ошибки и сообщением.


@RestControllerAdvice
class GlobalExceptionHandler {

    // 404 Not Found
    @ExceptionHandler(NotFoundException::class)
    fun handlerNotFound(ex: NotFoundException) : ResponseEntity<Map<String, Any?>> {
        val body = mapOf(
            "status" to 404,
            "error" to "Not Found",
            "message" to ex.message
        )
        return ResponseEntity(body, HttpStatus.NOT_FOUND)
    }

    // 400 - Валидация для @Valid в контроллерах
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

    // 400 - Валидация для @Validated на методах сервисов
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

    // 500 Internal Server Error для всех остальных исключений
    @ExceptionHandler(Exception::class)
    fun handlerInternal(ex: Exception) : ResponseEntity<Map<String, Any?>> {
        val body = mapOf(
            "status" to 500,
            "error" to "Internal Server Error",
            "message" to ex.message
        )
        ex.printStackTrace() // для логов
        return ResponseEntity(body, HttpStatus.INTERNAL_SERVER_ERROR)
    }


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