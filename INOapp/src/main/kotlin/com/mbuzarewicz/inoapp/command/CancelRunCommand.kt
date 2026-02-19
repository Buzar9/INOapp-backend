package com.mbuzarewicz.inoapp.command

data class CancelRunCommand(
    val runId: String,
    val reporter: String
)
