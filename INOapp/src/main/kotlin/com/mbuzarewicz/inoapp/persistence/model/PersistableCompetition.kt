package com.mbuzarewicz.inoapp.persistence.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableCompetition(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val adminPasswordHash: String = "",
)
