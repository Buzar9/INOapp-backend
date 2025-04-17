package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ScanTrustValidationResult.*
import com.mbuzarewicz.inoapp.ValidationResult.*
import com.mbuzarewicz.inoapp.ValidationResultStatus.*
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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
                "lastCheckpointTimestamp" to lastCheckpointTimestamp.toDailyHourFormat(),
                "scanCheckpointTimestamp" to scanCheckpointTimestamp.toDailyHourFormat()
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
                "mainRunTime" to mainRunTime.toAbsoluteHoursMinutesSecondsFormat()
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

    private fun Long.toDailyHourFormat(): String {
        val zoneId = ZoneId.of("Europe/Warsaw")
        val dateTime = Instant.ofEpochMilli(this).atZone(zoneId).toLocalDateTime()
        return dateTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"))
    }

    private fun Long.toAbsoluteHoursMinutesSecondsFormat(): String {
        val seconds = this / 1000
        val duration = Duration.ofSeconds(seconds)
        val hours = duration.toHours()
        val minutes = duration.toMinutes() % 60
        val secs = duration.seconds % 60

        return "%02d:%02d:%02d".format(hours, minutes, secs)
    }
}