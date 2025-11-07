package com.mbuzarewicz.inoapp.command

data class AddRouteCommand(
    val routeName: String,
    val backgroundMapId: String,
    val competitionId: String,
)
