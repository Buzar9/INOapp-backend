package com.mbuzarewicz.inoapp.persistence.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableRoute(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val stations: List<PersistableStation> = listOf(),
    val backgroundMapId: String = "",
    val competitionId: String = "",
    val active: Boolean = true,
)
