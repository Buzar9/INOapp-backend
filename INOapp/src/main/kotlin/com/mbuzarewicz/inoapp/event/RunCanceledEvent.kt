package com.mbuzarewicz.inoapp.event

import com.mbuzarewicz.inoapp.RunStatus

class RunCanceledEvent(
    val runId: String,
    val status: RunStatus
)
