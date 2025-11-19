package com.mbuzarewicz.inoapp.peristance.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableBackgroundMap(
    @DocumentId
    val id: String = "",
    val name: String = "",
    val fileSize: Long = 0L,
    val minZoom: Int = 0,
    val maxZoom: Int = 0,
    val northEast: PersistableLocation = PersistableLocation(0.0, 0.0),
    val southWest: PersistableLocation = PersistableLocation(0.0, 0.0),
    val active: Boolean = true,
)
