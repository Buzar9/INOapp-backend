package com.mbuzarewicz.inoapp

data class AddPenaltyCommand(
    val runId: String,
    val penaltyId: String,
    val offenseValue: String,
    val cause: PenaltyCause,
    val timestamp: Long,
)
