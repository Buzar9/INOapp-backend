package com.mbuzarewicz.inoapp

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableConstantStation(
//    dodo
//    @DocumentId
//    val id: String = "",
    val stationId: String = "",
//    val routeName: String = "",
    val location: PersistableLocation = PersistableLocation(),
    val type: String = "",
)