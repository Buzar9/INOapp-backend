package com.mbuzarewicz.inoapp

data class ValidationStationResultView(
    val checkpointId: String,
    val type: String,
    val status: String,
    val details: String,
)
