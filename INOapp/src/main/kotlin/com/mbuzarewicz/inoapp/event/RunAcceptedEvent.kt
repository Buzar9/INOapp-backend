package com.mbuzarewicz.inoapp.event

import com.mbuzarewicz.inoapp.RunStatus

data class RunAcceptedEvent(
    val runId: String,
    val status: RunStatus
)
