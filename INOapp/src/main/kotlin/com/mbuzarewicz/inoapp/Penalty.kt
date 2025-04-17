package com.mbuzarewicz.inoapp

data class Penalty(
//    uuid generowany na froncie, żeby sie nie duplikowaly
    val id: String,
    val timePenalty: Long,
    val cause: PenaltyCause,
    val details: String? = null,
)