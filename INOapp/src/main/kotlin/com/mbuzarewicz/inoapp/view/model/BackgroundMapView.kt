package com.mbuzarewicz.inoapp.view.model

data class BackgroundMapView(
    val id: String,
    val name: String,
    val fileUrl: String,
    val minZoom: Int,
    val maxZoom: Int,
    val northEast: List<Double>,
    val southWest: List<Double>,
)
