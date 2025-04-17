package com.mbuzarewicz.inoapp

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableRunReadModel(
    @DocumentId
    val id: String = "",
    val nickname: String = "",
    val team: String = "",
    val routeName: String = "",
    val competitionCategory: String = "",
    var status: RunStatus? = null,
    var startTime: Long = 0,
    val finishTime: Long = 0,
    val mainTime: Long = 0,
    val totalTime: Long = 0,
    val visitedCheckpointsNumber: Int = 0,
    val visitedCheckpoints: List<PersistableCheckpoint> = listOf(),
    val stationsValidationResults: Map<String, List<PersistableValidationResult>> = mapOf(),
    val penalties: List<PersistablePenalty> = listOf(),
)
