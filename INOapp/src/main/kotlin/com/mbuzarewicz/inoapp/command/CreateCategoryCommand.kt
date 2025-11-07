package com.mbuzarewicz.inoapp.command

data class CreateCategoryCommand(
    val name: String,
    val competitionId: String,
    val routeId: String,
    val maxTime: Long,
//    dodo tu nie trzeba podawać backgroundMapId, bo route to powinno miec
    val backgroundMapId: String,
)
