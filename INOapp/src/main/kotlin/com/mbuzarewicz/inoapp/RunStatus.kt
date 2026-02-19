package com.mbuzarewicz.inoapp

enum class RunStatus {
    CANCELED,
    FINISHED,
    INITIATED,
    STARTED;

    companion object {
        fun RunStatus.isAfterActivation() = this == STARTED || this == FINISHED
    }
}