package com.mbuzarewicz.inoapp.view.model

import com.mbuzarewicz.inoapp.domain.model.vo.Velocity

data class RunTrackSegmentView(
    val startPoint: GeometryView,
    val endPoint: GeometryView,
    val velocity: Velocity,
)