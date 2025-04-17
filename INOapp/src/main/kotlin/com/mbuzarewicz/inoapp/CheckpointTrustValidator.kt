package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ScanTrustValidationResult.TooFarScan
import com.mbuzarewicz.inoapp.ScanTrustValidationResult.TooShortTimeBetweenScans
import com.mbuzarewicz.inoapp.ValidationResultStatus.*

class CheckpointTrustValidator {

    private val minimumTimeBetweenCheckpoints: Long = 300
    private val geoPositionCalculator = GeoPositionCalculator()

    fun validate(
        lastCheckpointTimestamp: Long?,
        checkpointTimestamp: Long,
        checkpointLocation: Location,
        constantCheckpointLocation: Location
    ): List<ScanTrustValidationResult> {
        val result: MutableList<ScanTrustValidationResult> = mutableListOf()
        return result
            .checkTimeBetweenLastScannedCheckpoint(lastCheckpointTimestamp, checkpointTimestamp)
            .checkIfScanLocationIsNotTooFar(checkpointLocation, constantCheckpointLocation)
    }

    private fun MutableList<ScanTrustValidationResult>.checkTimeBetweenLastScannedCheckpoint(
        lastCheckpointTimestamp: Long?,
        checkpointTimestamp: Long,
    ): MutableList<ScanTrustValidationResult> {
        val minimumValidTime = lastCheckpointTimestamp?.plus(minimumTimeBetweenCheckpoints)

//        dodo to zawsze przechodzi
        val lastCheckpointTimestampDetails = lastCheckpointTimestamp ?: 0L

        val resultStatus = if (minimumValidTime == null || checkpointTimestamp < minimumValidTime) PASS else FAIL

        this.add(
            TooShortTimeBetweenScans(
                resultStatus = resultStatus,
                lastCheckpointTimestamp = lastCheckpointTimestampDetails,
                scanCheckpointTimestamp = checkpointTimestamp
            )
        )

        return this
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