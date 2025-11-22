package com.mbuzarewicz.inoapp.view.model

data class ConsolidatedStationView(
    val id: String,
    val name: String,
    val type: String,
    val note: String,
    val accuracy: Double,
    val isMounted: Boolean,
    val geometry: GeometryView,
    val routeId: String,
    val routeName: String,
)
