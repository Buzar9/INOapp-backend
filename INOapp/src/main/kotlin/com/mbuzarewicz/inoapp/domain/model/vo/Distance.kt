package com.mbuzarewicz.inoapp.domain.model.vo

import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit.KILOMETERS
import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit.METERS

data class Distance(
//    dodo BigDecimal
    val value: Double,
    val unit: DistanceUnit,
) : Comparable<Distance> {

    companion object {
        fun sumInUnit(distances: List<Distance>, unit: DistanceUnit = METERS): Distance {
            val totalMeters = distances.sumOf { it.toMeters() }
            val valueInUnit = when (unit) {
                METERS -> totalMeters
                KILOMETERS -> totalMeters / 1000.0
            }
            return Distance(value = valueInUnit, unit = unit)
        }
    }

    fun toMeters(): Double = when (unit) {
        METERS -> value
        KILOMETERS -> value * 1000.0
    }

    override fun compareTo(other: Distance): Int {
        val thisInMeters = toMeters()
        val otherInMeters = other.toMeters()
        return thisInMeters.compareTo(otherInMeters)
    }
}
