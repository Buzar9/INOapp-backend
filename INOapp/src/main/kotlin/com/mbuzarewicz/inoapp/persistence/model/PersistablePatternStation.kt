package com.mbuzarewicz.inoapp.persistence.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistablePatternStation(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val type: String = "",
    val location: PersistableLocation = PersistableLocation(),
    val note: String = ""
)