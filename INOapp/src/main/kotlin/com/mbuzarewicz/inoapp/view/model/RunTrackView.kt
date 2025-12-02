package com.mbuzarewicz.inoapp.view.model

import com.mbuzarewicz.inoapp.domain.model.RunTrackStats

data class RunTrackView(
    val runId: String,
    val segments: List<RunTrackSegmentView>,
    val stats: RunTrackStats?,
)
