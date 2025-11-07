package com.mbuzarewicz.inoapp.view.model

data class GeoGeometryView(
    val type: String = "Point",
    val coordinates: List<Double>
)
