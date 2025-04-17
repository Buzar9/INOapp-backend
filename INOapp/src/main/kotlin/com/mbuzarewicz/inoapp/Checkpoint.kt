package com.mbuzarewicz.inoapp

data class Checkpoint(
    val checkpointId: CheckpointId,
    val location: Location,
    val timestamp: Long,
)
