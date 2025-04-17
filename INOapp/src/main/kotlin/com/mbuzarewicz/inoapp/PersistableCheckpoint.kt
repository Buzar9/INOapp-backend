package com.mbuzarewicz.inoapp

data class PersistableCheckpoint(
    val checkpointId: PersistableCheckpointId = PersistableCheckpointId(),
    val location: PersistableLocation = PersistableLocation(),
    val timestamp: Long = 0,
)