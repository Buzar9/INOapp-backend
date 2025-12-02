package com.mbuzarewicz.inoapp.domain.model

import com.mbuzarewicz.inoapp.domain.model.vo.Distance
import com.mbuzarewicz.inoapp.domain.model.vo.Duration
import com.mbuzarewicz.inoapp.domain.model.vo.Velocity

data class RunTrackSegment(
    val startPoint: Location,
    val endPoint: Location,
    val startTime: Long,
    val endTime: Long,
    val distance: Distance,
    val durationMillis: Duration,
    val velocity: Velocity,
)