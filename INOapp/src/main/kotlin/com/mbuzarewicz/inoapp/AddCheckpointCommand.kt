package com.mbuzarewicz.inoapp

data class AddCheckpointCommand(
    val runId: String,
    val checkpointId: String,
    val routeName: String,
    val location: Location,
    val timestamp: Long,
)