package com.mbuzarewicz.inoapp.persistence.model

data class PersistableRunTrackPoint(
    val timestamp: Long = 0,
    val location: PersistableLocation = PersistableLocation(0.0, 0.0, 0.0)
)
