package com.mbuzarewicz.inoapp.peristance.model

data class PersistableStation(
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val location: PersistableLocation = PersistableLocation(),
    val note: String = ""
)