package com.mbuzarewicz.inoapp.event

import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.domain.model.ControlPoint

sealed class AddedControlPointEvent(
    val runId: String,
)

class RunStartedEvent(
    runId: String,
    val controlPoints: List<ControlPoint>,
    val startTime: Long,
    val status: RunStatus,
) : AddedControlPointEvent(runId)

class AddedCheckpointEvent(
    runId: String,
    val controlPoints: List<ControlPoint>,
) : AddedControlPointEvent(runId)

class RunFinishedEvent(
    runId: String,
    val controlPoints: List<ControlPoint>,
    val status: RunStatus,
    val finishTime: Long,
    val mainTime: Long,
) : AddedControlPointEvent(runId)
