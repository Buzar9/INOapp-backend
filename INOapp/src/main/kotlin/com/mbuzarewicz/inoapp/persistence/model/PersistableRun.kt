package com.mbuzarewicz.inoapp.persistence.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableRun(
    @DocumentId
    val id: String = "",
    val categoryId: String = "",
    val competitionId: String = "",
    val stations: List<PersistableStation> = listOf(),
    val controlPoints: List<PersistableControlPoint> = listOf(),
    val startTime: Long? = null,
    val finishTime: Long? = null,
    val status: String = "",
)