package com.mbuzarewicz.inoapp.domain.model

import com.mbuzarewicz.inoapp.domain.model.vo.Size

data class BackgroundMap(
    val id: String,
    val name: String,
    val competitionId: String,
    val fileSize: Size,
    val zoomsSize: Map<Int, Size>,
    val minZoom: Int,
    val maxZoom: Int,
    val northEast: Location,
    val southWest: Location,
    val isActive: Boolean = true
)
