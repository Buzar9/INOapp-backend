package com.mbuzarewicz.inoapp.event

import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.domain.model.ControlPoint

data class RunInitiatedEvent(
    val runId: String,
    val categoryId: String,
    val competitionId: String,
    val controlPoints: List<ControlPoint>,
    val participantName: String,
    val participantUnit: String,
    val status: RunStatus,
)