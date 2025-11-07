package com.mbuzarewicz.inoapp.view.model

data class GeoView(
    val type: String = "Feature",
    val geometry: GeoGeometryView,
    val properties: Map<String, String>,
)
