package com.mbuzarewicz.inoapp.persistence.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableRunTrack(
    @DocumentId
    val id: String = "",
    val points: List<PersistableRunTrackPoint> = emptyList()
)
