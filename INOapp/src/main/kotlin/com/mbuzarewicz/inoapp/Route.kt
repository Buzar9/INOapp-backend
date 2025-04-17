package com.mbuzarewicz.inoapp

data class Route(
    val databaseId: String,
    val routeName: String,
    val stations: List<ConstantStation>,
//    dodo
//    val penalties: Map<PenaltyCause, TimePenaltyRule>
)
