package com.mbuzarewicz.inoapp.controller

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        println("Bad Request: ${e.message}")
        return ResponseEntity(ErrorResponse(400, "Bad Request", e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<ErrorResponse> {
        println("Internal Server Error: ${e.message}")
        return ResponseEntity(ErrorResponse(500, "Internal Server Error", e.message), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}