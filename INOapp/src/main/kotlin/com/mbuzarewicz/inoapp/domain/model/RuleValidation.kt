package com.mbuzarewicz.inoapp.domain.model

data class RuleValidation(
    val type: RuleType,
    val result: RuleValidationResult,
)