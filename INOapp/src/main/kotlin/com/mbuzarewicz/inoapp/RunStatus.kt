package com.mbuzarewicz.inoapp

enum class RunStatus {
    INITIATED, STARTED, FINISHED;

    companion object {
        fun RunStatus.isAfterActivation() = this == STARTED || this == FINISHED
    }
}