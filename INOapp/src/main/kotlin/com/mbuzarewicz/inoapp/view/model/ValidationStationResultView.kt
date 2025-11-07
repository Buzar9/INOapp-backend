package com.mbuzarewicz.inoapp.view.model

data class ValidationStationResultView(
    val checkpointId: String,
    val type: String,
    val status: String,
    val details: String,
)
