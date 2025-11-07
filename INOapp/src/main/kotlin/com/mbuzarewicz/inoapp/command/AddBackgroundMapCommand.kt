package com.mbuzarewicz.inoapp.command

data class AddBackgroundMapCommand(
    val name: String,
    val minZoom: Int,
    val maxZoom: Int,
    val competitionId: String,
)
