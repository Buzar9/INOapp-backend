package com.mbuzarewicz.inoapp.domain.model

data class Route(
    val id: String,
    val name: String,
    val stations: List<Station>,
    val backgroundMapId: String,
    val competitionId: String,
    val isActive: Boolean,
)
