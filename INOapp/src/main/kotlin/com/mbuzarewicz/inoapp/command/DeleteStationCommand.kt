package com.mbuzarewicz.inoapp.command

data class DeleteStationCommand(
    val routeId: String,
    val stationId: String,
)
