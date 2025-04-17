package com.mbuzarewicz.inoapp

data class InitiateRunCommand(
    val runId: String,
    val routeName: String,
    val competitionCategory: String,
    val nickname: String,
    val team: String,
)