package com.mbuzarewicz.inoapp.command

data class ToggleStationMountCommand(
    val routeId: String,
    val stationId: String,
)