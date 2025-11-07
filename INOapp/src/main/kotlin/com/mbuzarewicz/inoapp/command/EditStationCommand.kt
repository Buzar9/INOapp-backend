package com.mbuzarewicz.inoapp.command

import com.mbuzarewicz.inoapp.domain.model.Location

data class EditStationCommand(
    val routeId: String,
    val stationId: String,
    val name: String,
    val type: String,
    val location: Location,
    val note: String
)
