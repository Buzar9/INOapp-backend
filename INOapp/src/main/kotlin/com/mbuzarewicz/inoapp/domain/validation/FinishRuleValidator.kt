package com.mbuzarewicz.inoapp.domain.validation

import com.mbuzarewicz.inoapp.GeoPositionCalculator
import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.domain.model.RuleType
import com.mbuzarewicz.inoapp.domain.model.RuleType.IS_WITHIN_TOLERANCE_RANGE
import com.mbuzarewicz.inoapp.domain.model.RuleValidation
import com.mbuzarewicz.inoapp.domain.model.RuleValidationResult.*

class FinishRuleValidator {

    private val geoPositionCalculator = GeoPositionCalculator()
    private val maxRunningTime = 14400000

    fun validate(
        controlPointLocation: Location,
        stationLocation: Location,
        startTime: Long,
        finishTime: Long
    ): List<RuleValidation> {
        val result: MutableList<RuleValidation> = mutableListOf()
        return result
            .checkIfIsWithinToleranceRange(controlPointLocation, stationLocation)
            .checkIfFinishedWithinTimeLimit(startTime, finishTime)
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

    private fun MutableList<RuleValidation>.checkIfFinishedWithinTimeLimit(
        startTime: Long,
        finishTime: Long
    ): MutableList<RuleValidation> {
        val mainRunTime = finishTime - startTime

        val result = if (mainRunTime < maxRunningTime) PASSED else FAILED
        this.add(
            RuleValidation(
                type = RuleType.FINISHED_WITHIN_TIME_LIMIT,
                result = result
            )
        )
        return this
    }
}