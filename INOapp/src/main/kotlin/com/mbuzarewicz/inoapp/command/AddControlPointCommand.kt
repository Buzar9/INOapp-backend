package com.mbuzarewicz.inoapp.command

import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.domain.model.Station

data class AddControlPointCommand(
    val runId: String,
    val stationId: String,
    val location: Location?,
    val timestamp: Long?,
    val reporter: String,
    val stations: MutableList<Station>,
)
