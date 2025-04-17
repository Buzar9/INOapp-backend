package com.mbuzarewicz.inoapp

import com.google.cloud.firestore.annotation.DocumentId

data class PersistablePenalty(
    @DocumentId
    val id: String = "",
    val timePenalty: Long = 0,
    val cause: PenaltyCause? = null,
    val details: String? = null,
)