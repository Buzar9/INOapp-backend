package com.mbuzarewicz.inoapp

data class PersistableValidationResult(
    val type: StationValidationType? = null,
    val details: Map<String, String> = mapOf(),
    val resultType: String = ""
)
