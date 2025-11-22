package com.mbuzarewicz.inoapp.view.model

data class GeoView(
    val type: String = "Feature",
    val geometry: GeometryView,
    val properties: Map<String, String>,
)
