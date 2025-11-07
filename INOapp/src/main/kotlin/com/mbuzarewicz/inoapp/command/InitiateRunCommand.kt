package com.mbuzarewicz.inoapp.command

data class InitiateRunCommand(
    val categoryId: String,
    val participantName: String,
    val participantUnit: String,
    val competitionId: String
)