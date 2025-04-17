package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ScanTrustValidationResult.*
import com.mbuzarewicz.inoapp.ValidationResult.*
import com.mbuzarewicz.inoapp.ValidationResultStatus.*

class ScanTrustValidationResultRunMapper {

    fun map(scanValidation: ScanTrustValidationResult): ValidationResult {
        return when (scanValidation) {
            is TooFarScan -> map(scanValidation)
            is TooShortTimeBetweenScans -> map(scanValidation)
            is RunningTimeExceededLimit -> map(scanValidation)
        }
    }

    private fun map(scanValidation: TooFarScan): ValidationResult {
        return with(scanValidation) {
            val details = mapOf("distance" to distance.toString(), "tolerance" to tolerance.toString())

            when (resultStatus) {
                PASS -> Pass(
                    type = type,
                    details = details
                )

                FAIL -> Fail(
                    type = type,
                    details = details
                )

                INSUFFICIENT_DATA -> InsufficientData(
                    type = type,
                    details = details
                )
            }
        }
    }

    private fun map(scanValidation: TooShortTimeBetweenScans): ValidationResult {
        return with(scanValidation) {
            val details = mapOf(
                "lastCheckpointTimestamp" to lastCheckpointTimestamp.toString(),
                "scanCheckpointTimestamp" to scanCheckpointTimestamp.toString()
            )

            when (resultStatus) {
                PASS -> Pass(
                    type = type,
                    details = details
                )

                FAIL -> Fail(
                    type = type,
                    details = details
                )

                INSUFFICIENT_DATA -> InsufficientData(
                    type = type,
                    details = details
                )
            }
        }
    }

    private fun map(scanValidation: RunningTimeExceededLimit): ValidationResult {
        return with(scanValidation) {
            val details = mapOf(
                "mainRunTime" to mainRunTime.toString()
            )

            when (resultStatus) {
                PASS -> Pass(
                    type = type,
                    details = details
                )

                FAIL -> Fail(
                    type = type,
                    details = details
                )

                INSUFFICIENT_DATA -> InsufficientData(
                    type = type,
                    details = details
                )
            }
        }
    }
}