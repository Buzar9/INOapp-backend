package com.mbuzarewicz.inoapp.peristance.model

import com.mbuzarewicz.inoapp.domain.model.StationValidationType

data class PersistableValidationResult(
    val type: StationValidationType? = null,
    val details: Map<String, String> = mapOf(),
    val resultType: String = ""
)
