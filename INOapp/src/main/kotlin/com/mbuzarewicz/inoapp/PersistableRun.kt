package com.mbuzarewicz.inoapp

import com.google.cloud.firestore.annotation.DocumentId
import com.mbuzarewicz.inoapp.RunStatus.INITIATED

data class PersistableRun(
    @DocumentId
    val id: String = "",
    val routeName: String = "",
    val competitionCategory: String = "",
    val constantStations: List<PersistableConstantStation> = listOf(),
    val visitedCheckpoints: List<PersistableCheckpoint> = listOf(),
    val stationsValidationResults: Map<String, List<PersistableValidationResult>> = mapOf(),
    val penalties: List<PersistablePenalty> = listOf(),
    val startTime: Long = 0L,
    val finishTime: Long = 0L,
    val status: RunStatus = INITIATED,
)