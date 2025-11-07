package com.mbuzarewicz.inoapp.peristance.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableCategory(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val competitionId: String = "",
    val routeId: String = "",
    val maxTime: Long = 0L,
    val backgroundMapId: String = "",
)
