package com.mbuzarewicz.inoapp.controller

data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String?
)