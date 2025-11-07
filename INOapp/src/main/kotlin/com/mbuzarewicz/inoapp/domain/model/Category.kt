package com.mbuzarewicz.inoapp.domain.model

data class Category(
    val id: String,
    val name: String,
    val competitionId: String,
    val routeId: String,
    val maxTime: Long,
    val backgroundMapId: String,
)
