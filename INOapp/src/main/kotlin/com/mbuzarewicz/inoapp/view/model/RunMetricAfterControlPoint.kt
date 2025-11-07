package com.mbuzarewicz.inoapp.view.model

data class RunMetricAfterControlPoint(
    val startTime: Long?,
    val finishTime: Long?,
    val mainTime: Long?,
    val checkpointsNumber: Int?,
    val wasActivate: Boolean,
    val isFinished: Boolean,
)
