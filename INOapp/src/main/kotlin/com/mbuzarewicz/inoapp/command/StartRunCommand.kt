package com.mbuzarewicz.inoapp.command

import com.mbuzarewicz.inoapp.domain.model.Location

class StartRunCommand(
    val runId: String,
    val stationId: String,
    val location: Location,
    val timestamp: Long,
)