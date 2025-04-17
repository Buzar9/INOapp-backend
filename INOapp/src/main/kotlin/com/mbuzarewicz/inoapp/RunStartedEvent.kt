package com.mbuzarewicz.inoapp

data class RunStartedEvent(
    val runId: String,
    val stationsValidationResults: Map<CheckpointId, List<ScanTrustValidationResult>>,
    val penalties: MutableList<Penalty>,
    val startTime: Long,
    val status: RunStatus,
)
