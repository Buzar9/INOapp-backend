package com.mbuzarewicz.inoapp

sealed class ValidationResult(val type: StationValidationType, val details: Map<String,String>) {
    class Pass(type: StationValidationType, details: Map<String,String>) : ValidationResult(type, details)
    class Fail(type: StationValidationType, details: Map<String,String>) : ValidationResult(type, details)
    class InsufficientData(type: StationValidationType, details: Map<String,String>) : ValidationResult(type, details)
}