package com.mbuzarewicz.inoapp.domain.model

data class Station(
    val id: String,
    val name: String,
    val type: StationType,
    val location: Location,
    val note: String,
    val isMounted: Boolean = false
)