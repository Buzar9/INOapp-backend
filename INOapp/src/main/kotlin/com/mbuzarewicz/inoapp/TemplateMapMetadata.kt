package com.mbuzarewicz.inoapp

data class TemplateMapMetadata(
    val epsg: String,
    val upperRight: List<Double>,
    val lowerRight: List<Double>,
    val upperLeft: List<Double>,
    val lowerLeft: List<Double>,
)
