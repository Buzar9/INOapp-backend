package com.mbuzarewicz.inoapp.command

import com.mbuzarewicz.inoapp.domain.model.Location

data class AddCheckpointCommand(
    val runId: String,
    val stationId: String,
    val location: Location,
    val timestamp: Long,
)