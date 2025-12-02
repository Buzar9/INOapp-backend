package com.mbuzarewicz.inoapp.persistence.model

data class PersistableControlPoint(
    val stationId: String = "",
    val name: String = "",
    val type: String = "",
    val location: PersistableLocation = PersistableLocation(),
    val timestamp: Long = 0,
    val ruleValidations: List<PersistableRuleValidation> = listOf(),
)