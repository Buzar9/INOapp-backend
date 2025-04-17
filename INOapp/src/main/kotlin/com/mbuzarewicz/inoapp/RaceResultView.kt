package com.mbuzarewicz.inoapp

data class RaceResultView(
    val nickname: String,
    val team: String,
    val routeName: String,
    val competitionCategory: String,
    val status: String,
    val startTime: String,
    val finishTime: String,
    val mainTime: String,
    val totalTime: String,
    val visitedCheckpointsNumber: String,
    val validationsStationResult: List<ValidationStationResultView>
)
