package com.mbuzarewicz.inoapp

data class FinishRunCommand(
    val runId: String,
    val location: Location,
    val timestamp: Long,
)