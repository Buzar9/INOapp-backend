package com.mbuzarewicz.inoapp.persistence.model

data class PersistableLocation(
    val lat: Double = 0.0,
    val lng: Double = 0.0,
    val accuracy: Double = 0.0,
)
