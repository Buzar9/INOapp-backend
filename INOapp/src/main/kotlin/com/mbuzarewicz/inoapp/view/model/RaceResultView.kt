package com.mbuzarewicz.inoapp.view.model

data class RaceResultView(
    val runId: String,
    val participantNickname: String,
    val participantUnit: String,
    val categoryName: String,
    val routeId: String,
    val translatedStatus: String,
    val status: String,
    val startTime: String,
    val finishTime: String,
    val mainTime: String,
    val controlPoints: List<ControlPointView>,
    val runTrackId: String? = null
)
