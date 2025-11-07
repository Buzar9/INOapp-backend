package com.mbuzarewicz.inoapp.domain.model

data class ControlPoint(
    val stationId: String,
    val name: String,
    val type: StationType,
    val location: Location,
    val timestamp: Long,
    val ruleValidation: List<RuleValidation>
)
