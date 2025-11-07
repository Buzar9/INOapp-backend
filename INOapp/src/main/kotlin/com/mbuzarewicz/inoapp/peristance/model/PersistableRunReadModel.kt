package com.mbuzarewicz.inoapp.peristance.model

import com.google.cloud.firestore.annotation.DocumentId

data class PersistableRunReadModel(
    @DocumentId
    val id: String = "",
    val categoryId: String = "",
    val competitionId: String = "",
    val controlPoints: List<PersistableControlPoint> = listOf(),
    val participantNickname: String = "",
    val participantUnit: String = "",
    val status: String = "",
    val startTime: Long? = null,
    val finishTime: Long? = null,
    val mainTime: Long? = null,
)
