package com.mbuzarewicz.inoapp

data class RunFinishedEvent(
    val runId: String,
    val status: RunStatus,
    val finishTime: Long,
    val mainTime: Long,
    val totalTime: Long,
    val stationsValidationResults: Map<CheckpointId, List<ScanTrustValidationResult>>,
)