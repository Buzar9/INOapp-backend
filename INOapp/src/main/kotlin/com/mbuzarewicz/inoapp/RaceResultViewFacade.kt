package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class RaceResultViewFacade(
    private val runReadModelFacade: RunReadModelFacade,
    private val translator: TranslationService,
//    private val translationServiceDodo: TranslationServiceDodo
) {

    fun getResults(): List<RaceResultView> {
        val runReadModels = runReadModelFacade.getAll()
        val results = runReadModels.map {
//            dodo troche kupa
            val startTimeToDisplay = if (it.startTime != 0L) it.startTime.toDailyHourFormat() else "---"
            val finishTimeToDisplay = if (it.finishTime != 0L) it.finishTime.toDailyHourFormat() else "---"
            val mainTimeToDisplay = if (it.mainTime != 0L) it.mainTime.toAbsoluteHoursMinutesSecondsFormat() else "---"
            val totalTimeToDisplay =
                if (it.totalTime != 0L) it.totalTime.toAbsoluteHoursMinutesSecondsFormat() else "---"
            RaceResultView(
                nickname = it.nickname,
                team = it.team,
                routeName = translator.translate("route-name", it.routeName),
                competitionCategory = translator.translate("competition-category", it.competitionCategory),
                status = translator.translate("run-status", it.status.toString()),
                startTime = startTimeToDisplay,
                finishTime = finishTimeToDisplay,
                mainTime = mainTimeToDisplay,
                totalTime = totalTimeToDisplay,
                visitedCheckpointsNumber = it.visitedCheckpointsNumber.toString(),
                validationsStationResult = mapToValidationStationResultViewList(
                    it.stationsValidationResults,
                    it.visitedCheckpoints
                )
            )
        }

        return results.sortedWith(
            compareByDescending<RaceResultView> { it.visitedCheckpointsNumber }.thenBy { it.totalTime }
        )
    }

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

    private fun mapToValidationStationResultViewList(
        validationMap: Map<CheckpointId, List<ValidationResult>>,
        checkpoints: List<Checkpoint>
    ): List<ValidationStationResultView> {
        val resultList = mutableListOf<ValidationStationResultView>()

        validationMap.forEach { (checkpointId, validationResults) ->
            validationResults.forEach { validationResult ->
                val status = when (validationResult) {
                    is ValidationResult.Pass -> "Pass"
                    is ValidationResult.Fail -> "Fail"
                    is ValidationResult.InsufficientData -> "InsufficientData"
                }

//                dodo może w readmodelu timestamp powinien byc przyklejony do resultatu validacji?
                val view = ValidationStationResultView(
                    checkpointId = checkpointId.toStringId(),
                    type = translator.translate("station-validation-type", validationResult.type.toString()),
                    status = status,
                    details = validationResult.details.mapToString(),
                )
                resultList.add(view)
            }
        }
        return resultList
    }

    private fun Map<String, String>.mapToString(): String {
        return this.entries.joinToString(", ") { (key, value) -> "$key: $value" }
    }
}