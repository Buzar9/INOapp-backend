package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ScanTrustValidationResult.RunningTimeExceededLimit
import com.mbuzarewicz.inoapp.ScanTrustValidationResult.TooFarScan
import com.mbuzarewicz.inoapp.ValidationResultStatus.*

class FinishTrustValidator {

    private val geoPositionCalculator = GeoPositionCalculator()
    private val maxRunningTime = 14400000

    fun validate(
        checkpointLocation: Location,
        constantCheckpointLocation: Location,
        startTime: Long,
        finishTime: Long
    ): List<ScanTrustValidationResult> {
        val result: MutableList<ScanTrustValidationResult> = mutableListOf()
        return result
            .checkIfScanLocationIsNotTooFar(checkpointLocation, constantCheckpointLocation)
            .checkIfDisqualifiedByTime(startTime, finishTime)
    }

    private fun MutableList<ScanTrustValidationResult>.checkIfScanLocationIsNotTooFar(
        stationLocation: Location,
        constantStationLocation: Location,
    ): MutableList<ScanTrustValidationResult> {
        if (stationLocation.lat <= 0.0 || stationLocation.lng <= 0.0 ) {
            this.add(
                TooFarScan(
                    resultStatus = INSUFFICIENT_DATA,
                    distance = 0.0,
                    tolerance = 0.0
                )
            )
            return this
        }

        val distance = geoPositionCalculator.calculateDistance(
            stationLocation.lat,
            stationLocation.lng,
            constantStationLocation.lat,
            constantStationLocation.lng
        )
        val tolerance = geoPositionCalculator.calculateTolerance(
            stationLocation.accuracy,
            constantStationLocation.accuracy
        )

        val resultStatus = if (distance <= tolerance) PASS else FAIL
        this.add(
            TooFarScan(
                resultStatus = resultStatus,
                distance = distance,
                tolerance = tolerance
            )
        )
        return this
    }

    private fun MutableList<ScanTrustValidationResult>.checkIfDisqualifiedByTime(
        startTime: Long,
        finishTime: Long
    ): MutableList<ScanTrustValidationResult> {
        val mainRunTime = finishTime - startTime

        val resultStatus = if (mainRunTime < maxRunningTime) PASS else FAIL
        this.add(
            RunningTimeExceededLimit(
                resultStatus = resultStatus,
                mainRunTime = mainRunTime
            )
        )
        return this
    }
}