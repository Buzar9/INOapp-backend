package com.mbuzarewicz.inoapp

data class RunInitiatedEvent(
    val runId: String,
    val nickname: String,
    val team: String,
    val routeName: String,
    val competitionCategory: String,
    val status: RunStatus,
    val startTime: Long,
    val finishTime: Long,
    val mainTime: Long,
    val totalTime: Long,
    val visitedCheckpointsNumber: Int,
    val visitedCheckpoints: MutableList<Checkpoint>,
    val stationsValidationResults: MutableMap<CheckpointId, List<ValidationResult>>,
    val penalties: MutableList<Penalty>,
)