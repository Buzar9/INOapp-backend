package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ScanTrustValidationResult.*
import com.mbuzarewicz.inoapp.TimePresenter.Companion.formatToDailyHour
import com.mbuzarewicz.inoapp.ValidationResult.*
import com.mbuzarewicz.inoapp.ValidationResultStatus.*
import java.math.BigDecimal
import java.math.RoundingMode

class ScanTrustValidationResultRunReadModelMapper {

    fun map(scanValidation: ScanTrustValidationResult): ValidationResult {
        return when (scanValidation) {
            is TooFarScan -> map(scanValidation)
            is TooShortTimeBetweenScans -> map(scanValidation)
            is RunningTimeExceededLimit -> map(scanValidation)
        }
    }

    //    dodo pozmieniac na te ładne wartości z ucietymi doublami i datami zamiast longow
    private fun map(scanValidation: TooFarScan): ValidationResult {
        return with(scanValidation) {
            val details = mapOf(
                "distance" to distance.prepareLengthMeasurement(),
                "tolerance" to tolerance.prepareLengthMeasurement()
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

    private fun map(scanValidation: TooShortTimeBetweenScans): ValidationResult {
        return with(scanValidation) {
            val details = mapOf(
                "lastCheckpointTimestamp" to formatToDailyHour(lastCheckpointTimestamp),
                "scanCheckpointTimestamp" to formatToDailyHour(scanCheckpointTimestamp)
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
                "mainRunTime" to TimePresenter.formatToAbsoluteHoursMinutesSeconds(mainRunTime)
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

    private fun Double.prepareLengthMeasurement() = BigDecimal(this).setScale(1, RoundingMode.HALF_UP).toString()
}