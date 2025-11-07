package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.domain.model.ControlPoint

data class RunReadModel(
    val id: String,
    val categoryId: String,
    val competitionId: String,
    val controlPoints: List<ControlPoint>,
    val participantName: String,
    val participantUnit: String,
    val status: RunStatus,
    val startTime: Long? = null,
    val finishTime: Long? = null,
    val mainTime: Long? = null,
)