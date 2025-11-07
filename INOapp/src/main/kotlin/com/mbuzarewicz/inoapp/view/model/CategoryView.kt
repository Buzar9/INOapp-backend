package com.mbuzarewicz.inoapp.view.model

data class CategoryView(
    val id: String,
    val name: String,
    val routeId: String,
    val routeName: String,
    val maxTime: Long,
    val backgroundMap: BackgroundMapView,
)
