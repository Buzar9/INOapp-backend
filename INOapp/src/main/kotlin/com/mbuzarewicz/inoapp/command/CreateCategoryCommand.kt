package com.mbuzarewicz.inoapp.command

data class CreateCategoryCommand(
    val name: String,
    val competitionId: String,
    val routeId: String,
)
