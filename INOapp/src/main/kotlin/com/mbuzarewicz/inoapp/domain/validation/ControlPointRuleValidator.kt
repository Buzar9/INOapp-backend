package com.mbuzarewicz.inoapp.domain.validation

import com.mbuzarewicz.inoapp.GeoPositionCalculator
import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.domain.model.RuleType.INTERVAL_IS_LONG_ENOUGH
import com.mbuzarewicz.inoapp.domain.model.RuleType.IS_WITHIN_TOLERANCE_RANGE
import com.mbuzarewicz.inoapp.domain.model.RuleValidation
import com.mbuzarewicz.inoapp.domain.model.RuleValidationResult.*

class ControlPointRuleValidator {

    private val minimumTimeBetweenCheckpoints: Long = 300
    private val geoPositionCalculator = GeoPositionCalculator()

    fun validate(
        lastControlPointTimestamp: Long?,
        controlPointTimestamp: Long,
        controlPointLocation: Location,
        stationLocation: Location
    ): List<RuleValidation> {
        val result: MutableList<RuleValidation> = mutableListOf()
        return result
            .checkIfIntervalIsLongEnough(lastControlPointTimestamp, controlPointTimestamp)
            .checkIfIsWithinToleranceRange(controlPointLocation, stationLocation)
    }

    private fun MutableList<RuleValidation>.checkIfIntervalIsLongEnough(
        lastControlPointTimestamp: Long?,
        controlPointTimestamp: Long,
    ): MutableList<RuleValidation> {
        val minimumValidTime = lastControlPointTimestamp?.plus(minimumTimeBetweenCheckpoints)

        val result = if (minimumValidTime == null || controlPointTimestamp < minimumValidTime) PASSED else FAILED

        this.add(
            RuleValidation(
                type = INTERVAL_IS_LONG_ENOUGH,
                result = result
            )
        )

        return this
    }

    private fun MutableList<RuleValidation>.checkIfIsWithinToleranceRange(
        controlPointLocation: Location,
        stationLocation: Location,
    ): MutableList<RuleValidation> {
        val type = IS_WITHIN_TOLERANCE_RANGE
        if (controlPointLocation.lat <= 0.0 || controlPointLocation.lng <= 0.0) {
            this.add(
                RuleValidation(
                    type = type,
                    result = INSUFFICIENT_DATA
                )
            )
            return this
        }

        val distance = geoPositionCalculator.calculateDistance(
            controlPointLocation.lat,
            controlPointLocation.lng,
            stationLocation.lat,
            stationLocation.lng
        )
        val tolerance = geoPositionCalculator.calculateTolerance(
            controlPointLocation.accuracy,
            stationLocation.accuracy
        )

        val result = if (distance <= tolerance) PASSED else FAILED
        this.add(
            RuleValidation(
                type = type,
                result = result
            )
        )

        return this
    }
}