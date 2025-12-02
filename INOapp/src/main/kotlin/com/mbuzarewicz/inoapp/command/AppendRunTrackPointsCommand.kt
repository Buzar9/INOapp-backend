package com.mbuzarewicz.inoapp.command

import com.mbuzarewicz.inoapp.domain.model.RunTrackPoint

data class AppendRunTrackPointsCommand(
    val runId: String,
    val points: List<RunTrackPoint>,
)
