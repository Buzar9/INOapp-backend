package com.mbuzarewicz.inoapp.domain.service

import com.mbuzarewicz.inoapp.domain.model.vo.*
import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit.KILOMETERS
import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit.METERS
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.HOURS
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.MILLISECONDS
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.SECONDS
import com.mbuzarewicz.inoapp.domain.model.vo.VelocityUnit.KILOMETERS_PER_HOUR
import com.mbuzarewicz.inoapp.domain.model.vo.VelocityUnit.METERS_PER_SECOND


class VelocityCalculator {
    fun calculateVelocity(
        distance: Distance,
        duration: Duration,
        targetUnit: VelocityUnit = KILOMETERS_PER_HOUR
    ): Velocity {
        val distanceInMeters = when (distance.unit) {
            METERS -> distance.value
            KILOMETERS -> distance.value * 1000.0
        }

        val durationInSeconds = when (duration.unit) {
            MILLISECONDS -> duration.value / 1000.0
            SECONDS -> duration.value.toDouble()
            HOURS -> duration.value * 3600.0
        }

        val metersPerSecond =
            if (durationInSeconds > 0) {
                distanceInMeters / durationInSeconds
            } else {
                0.0
            }

        val velocityValue = when (targetUnit) {
            METERS_PER_SECOND -> metersPerSecond
            KILOMETERS_PER_HOUR -> metersPerSecond * 3.6
        }

        return Velocity(value = velocityValue, unit = targetUnit)
    }
}



