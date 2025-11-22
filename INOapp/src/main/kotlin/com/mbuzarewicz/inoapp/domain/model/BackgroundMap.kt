package com.mbuzarewicz.inoapp.domain.model

data class BackgroundMap(
    val id: String,
    val name: String,
//    dodo dodac value object i niech sam sie wylicza na front to robi i jest latwe
    val fileSize: Long,
    val minZoom: Int,
    val maxZoom: Int,
    val northEast: Location,
    val southWest: Location,
    val isActive: Boolean = true
)
