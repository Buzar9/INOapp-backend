package com.mbuzarewicz.inoapp.persistence.model

data class PersistableStation(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val location: PersistableLocation = PersistableLocation(),
    val note: String = "",
    val mounted: Boolean = false
)