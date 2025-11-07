package com.mbuzarewicz.inoapp.domain.model

data class BackgroundMap(
    val id: String,
    val name: String,
    val fileUrl: String,
    val minZoom: Int,
    val maxZoom: Int,
    val northEast: Location,
    val southWest: Location,
)
