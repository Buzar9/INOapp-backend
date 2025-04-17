package com.mbuzarewicz.inoapp

data class CheckpointId(
    val id: String,
    val routeName: String,
) {

    fun toStringId() = "$routeName-$id"
}