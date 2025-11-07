package com.mbuzarewicz.inoapp.view.model

data class ControlPointView(
    val stationId: String,
    val name: String,
    val type: String,
    val timestamp: String,
    val ruleValidation: List<RuleValidationView>,
    val geoView: GeoView,
)
