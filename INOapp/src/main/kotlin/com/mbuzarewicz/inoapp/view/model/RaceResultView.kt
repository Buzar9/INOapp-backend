package com.mbuzarewicz.inoapp.view.model

data class RaceResultView(
    val participantNickname: String,
    val participantUnit: String,
    val categoryName: String,
    val routeId: String,
    val status: String,
    val startTime: String,
    val finishTime: String,
    val mainTime: String,
    val controlPoints: List<ControlPointView>
)
