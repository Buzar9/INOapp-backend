package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ScanTrustValidationResult.TooFarScan
import com.mbuzarewicz.inoapp.ValidationResultStatus.*

class StartTrustValidator {

    private val geoPositionCalculator = GeoPositionCalculator()

    fun validate(
        checkpointLocation: Location,
        constantCheckpointLocation: Location
    ): List<ScanTrustValidationResult> {
        val result: MutableList<ScanTrustValidationResult> = mutableListOf()
        return result
            .checkIfScanLocationIsNotTooFar(checkpointLocation, constantCheckpointLocation)
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
}