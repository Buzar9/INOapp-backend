package com.mbuzarewicz.inoapp.view.model

data class RouteView(
    val id: String,
    val name: String,
    val stations: List<GeoView>,
    val backgroundMap: BackgroundMapView,
)
