package com.grig.restapirecipes.security

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

//@ControllerAdvice
//class SecGlobalExceptionHandler {
//
//    @ExceptionHandler(AccessDeniedException::class)
//    fun handleAccessDenied(ex: AccessDeniedException): ResponseEntity<Map<String, String>> {
//        val body: Map<String, String> = mapOf(
//            "error" to "Access denied",
//            "message" to (ex.message ?: "No message")
//        )
//        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body)
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
//}