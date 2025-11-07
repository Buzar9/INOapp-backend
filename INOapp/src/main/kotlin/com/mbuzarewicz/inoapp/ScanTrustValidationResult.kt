package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.domain.model.StationValidationType
import com.mbuzarewicz.inoapp.domain.model.StationValidationType.*

sealed class ScanTrustValidationResult(val type: StationValidationType, val resultStatus: ValidationResultStatus) {
    class TooFarScan(
        resultStatus: ValidationResultStatus,
        val distance: Double,
        val tolerance: Double
    ) : ScanTrustValidationResult(type = SCAN_LOCATION_IS_TOO_FAR, resultStatus)

    class TooShortTimeBetweenScans(
        resultStatus: ValidationResultStatus,
        val lastCheckpointTimestamp: Long,
        val scanCheckpointTimestamp: Long
    ) : ScanTrustValidationResult(type = TIME_BETWEEN_LAST_SCANNED_CHECKPOINT, resultStatus)

    class RunningTimeExceededLimit(
        resultStatus: ValidationResultStatus,
        val mainRunTime: Long
    ) :
        ScanTrustValidationResult(type = RUNNING_TIME_EXCEEDED_LIMIT, resultStatus)
}
