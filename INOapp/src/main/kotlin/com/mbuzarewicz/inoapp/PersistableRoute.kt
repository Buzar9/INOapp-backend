package com.mbuzarewicz.inoapp

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableRoute(
    @DocumentId
    val id: String = "",
    val routeName: String = "",
    val stations: List<PersistableConstantStation> = listOf(),
//    dodo
//    val penalties: Map<String, PersistableTimePenaltyRule> = mapOf(),
)
