package com.mbuzarewicz.inoapp

data class AddedCheckpointEvent(
    val runId: String,
    val visitedCheckpoints: List<Checkpoint>,
    val stationsValidationResults: Map<CheckpointId, List<ScanTrustValidationResult>>,
)
