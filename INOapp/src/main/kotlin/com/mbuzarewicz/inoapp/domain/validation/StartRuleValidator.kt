package com.mbuzarewicz.inoapp.domain.validation

import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.domain.model.RuleType.IS_WITHIN_TOLERANCE_RANGE
import com.mbuzarewicz.inoapp.domain.model.RuleValidation
import com.mbuzarewicz.inoapp.domain.model.RuleValidationResult.*
import com.mbuzarewicz.inoapp.domain.service.DistanceCalculator

class StartRuleValidator {

    private val distanceCalculator = DistanceCalculator()

    fun validate(
        controlPointLocation: Location,
        stationLocation: Location
    ): List<RuleValidation> {
        val result: MutableList<RuleValidation> = mutableListOf()
        return result
            .checkIfIsWithinToleranceRange(controlPointLocation, stationLocation)
    }

    private fun MutableList<RuleValidation>.checkIfIsWithinToleranceRange(
        controlPointLocation: Location,
        stationLocation: Location,
    ): MutableList<RuleValidation> {
        if (controlPointLocation.lat <= 0.0 || controlPointLocation.lng <= 0.0) {
            this.add(
                RuleValidation(
                    type = IS_WITHIN_TOLERANCE_RANGE,
                    result = INSUFFICIENT_DATA
                )
            )
            return this
        }

        val distance = distanceCalculator.calculateDistance(
            controlPointLocation,
            stationLocation,
        )
        val tolerance = distanceCalculator.calculateTolerance(
            controlPointLocation.accuracy,
            stationLocation.accuracy
        )

        val result = if (distance <= tolerance) PASSED else FAILED
        this.add(
            RuleValidation(
                type = IS_WITHIN_TOLERANCE_RANGE,
                result = result
            )
        )

        return this
    }
}