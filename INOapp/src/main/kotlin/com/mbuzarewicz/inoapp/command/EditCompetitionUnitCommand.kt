package com.mbuzarewicz.inoapp.command

data class EditCompetitionUnitCommand(
    val id: String,
    val name: String,
    val competitionId: String = "Competition123",
)
