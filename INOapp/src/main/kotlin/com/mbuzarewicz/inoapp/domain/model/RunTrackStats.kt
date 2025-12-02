package com.mbuzarewicz.inoapp.domain.model

import com.mbuzarewicz.inoapp.domain.model.vo.Distance
import com.mbuzarewicz.inoapp.domain.model.vo.Duration
import com.mbuzarewicz.inoapp.domain.model.vo.Velocity

data class RunTrackStats(
    val totalDistance: Distance,
    val totalDuration: Duration,
    val averageSpeed: Velocity,
)
