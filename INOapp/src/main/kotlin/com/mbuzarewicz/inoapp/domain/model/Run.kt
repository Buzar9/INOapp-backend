package com.mbuzarewicz.inoapp.domain.model

import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.RunStatus.*
import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.command.CancelRunCommand
import com.mbuzarewicz.inoapp.domain.model.RuleType.IS_WITHIN_TOLERANCE_RANGE
import com.mbuzarewicz.inoapp.domain.model.RuleValidationResult.INSUFFICIENT_DATA
import com.mbuzarewicz.inoapp.domain.model.StationType.*
import com.mbuzarewicz.inoapp.domain.model.vo.Duration
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit
import com.mbuzarewicz.inoapp.domain.service.PositionCalculator
import com.mbuzarewicz.inoapp.domain.validation.ControlPointRuleValidator
import com.mbuzarewicz.inoapp.domain.validation.FinishRuleValidator
import com.mbuzarewicz.inoapp.domain.validation.StartRuleValidator
import com.mbuzarewicz.inoapp.event.*
import java.util.*

class Run private constructor(
//    dodo jak wylaniac zwyciescow? poki co idzie sortowana lista
    val id: String,
//    dodo run jako agregat wie jaka jest kategoria i competition i jak chcesz odpalac cala kategorie na raz to szukasz wszystkich agregatow w kategorii i na kazdym wykonujesz metoda ktora wbija status start enabled
    val competitionId: String,
    val controlPoints: MutableList<ControlPoint>,
//    dodo niby samotna wyspa ale kategorie to musi znac
    var categoryId: String,
    var status: RunStatus,
) {
    var startTime: Long? = null
        private set
    var finishTime: Long? = null
        private set

    private val startRuleValidator = StartRuleValidator()
    private val controlPointRuleValidator = ControlPointRuleValidator()
    private val finishRuleValidator = FinishRuleValidator()
    private val positionCalculator = PositionCalculator()

    companion object {
        fun initiate(
            participantName: String,
            participantUnit: String,
            categoryId: String,
            competitionId: String,
        ): Pair<Run, RunInitiatedEvent> {
            val runId = UUID.randomUUID().toString()
            return Pair(
                Run(
                    id = runId,
                    categoryId = categoryId,
                    competitionId = competitionId,
                    controlPoints = mutableListOf(),
                    status = INITIATED
                ),
                RunInitiatedEvent(
                    runId = runId,
                    categoryId = categoryId,
                    competitionId = competitionId,
                    controlPoints = emptyList(),
                    participantName = participantName,
                    participantUnit = participantUnit,
                    status = INITIATED,
                )
            )
        }

        fun recreate(
            id: String,
            categoryId: String,
            competitionId: String,
            controlPoints: MutableList<ControlPoint>,
            startTime: Long?,
            finishTime: Long?,
            status: RunStatus
        ): Run {
            val run = Run(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
                controlPoints = controlPoints,
                status = status
            )
            run.startTime = startTime
            run.finishTime = finishTime

            return run
        }
    }

    fun addControlPoint(command: AddControlPointCommand): AddedControlPointEvent? {
        if (command.stations.isEmpty()) return null

        val station = command.stations.firstOrNull { it.id == command.stationId }

        station ?: return null

        return when (station.type) {
            START_RUN -> {
                start(station, command.location, command.timestamp, command.reporter)
            }

            CHECKPOINT -> {
                addCheckpoint(station, command.location, command.timestamp, command.reporter)
            }

            FINISH_RUN -> {
                finish(station, command.location, command.timestamp, command.reporter)
            }
        }
    }

    fun cancel(): RunCanceledEvent {
        status = CANCELED

        return RunCanceledEvent(
            runId = id,
            status = status,
        )

    }

    //    dodo nie wiem czy nie kupa? czy mozna odpytywac agregatu o jego stan ?
    fun getMainTime(): Duration {
        if (startTime == null || finishTime == null) throw Exception("dodo")

        return Duration(finishTime!! - startTime!!, DurationUnit.MILLISECONDS)
    }

    private fun start(station: Station, location: Location?, timestamp: Long?, reporter: String): RunStartedEvent? {
        if (status != INITIATED) return null
        if (controlPoints.any { it.stationId == station.id }) return null

        if (timestamp == null) return null

        val ruleValidation = startRuleValidator.validate(location, station.location)
//        dodo co z tym mapperem i co z trzymaniem wynikow walidacji w agregacie? moze na event o wystartowaniu powinnien byc handler ktory to zlapie i zwaliduje? wtedy osobny model na walidacje i polityka zwyciezcow

        val validatedLocation = getValidLocationOrRandom(ruleValidation, station.location)

        val controlPoint = ControlPoint(
            stationId = station.id,
            name = station.name,
            type = station.type,
            location = validatedLocation,
            timestamp = timestamp,
            ruleValidation = ruleValidation,
            reporter = reporter
        )
        controlPoints.add(controlPoint)

        startTime = timestamp
        status = STARTED

        return RunStartedEvent(
            runId = id,
            controlPoints = controlPoints,
            startTime = timestamp,
            status = status
        )
    }

    private fun addCheckpoint(station: Station, location: Location?, timestamp: Long?, reporter: String): AddedCheckpointEvent? {
//       dodo tu jest kupa zabezpieczen ze ten user jest na srtingu
        if (status != STARTED && reporter.uppercase() == "USER") return null
        if (controlPoints.any { it.stationId == station.id }) return null

        val lastControlStationTimestamp = controlPoints.maxByOrNull { it.timestamp }?.timestamp
        val ruleValidation = controlPointRuleValidator.validate(
            lastControlStationTimestamp,
            timestamp ?: 0L,
            location,
            station.location
        )

        val validatedLocation = getValidLocationOrRandom(ruleValidation, station.location)

        val controlPoint = ControlPoint(
            stationId = station.id,
            name = station.name,
            type = station.type,
            location = validatedLocation,
            timestamp = timestamp ?: 0L,
            ruleValidation = ruleValidation,
            reporter = reporter
        )
        controlPoints.add(controlPoint)

        return AddedCheckpointEvent(
            runId = id,
            controlPoints = controlPoints,
        )
    }

    private fun finish(station: Station, location: Location?, timestamp: Long?, reporter: String): RunFinishedEvent? {
        if (status != STARTED) return null

        if (timestamp == null) return null

        val ruleValidation =
            finishRuleValidator.validate(location, station.location, startTime!!, timestamp)

        status = FINISHED

        finishTime = timestamp

        val validatedLocation = getValidLocationOrRandom(ruleValidation, station.location)

        val controlPoint = ControlPoint(
            stationId = station.id,
            name = station.name,
            type = station.type,
            location = validatedLocation,
            timestamp = timestamp,
            ruleValidation = ruleValidation,
            reporter = reporter
        )
        controlPoints.add(controlPoint)

        return RunFinishedEvent(
            runId = id,
            controlPoints = controlPoints,
            status = status,
            finishTime = timestamp,
//            dodo zmienic na Duration
            mainTime = getMainTime().value,
        )
    }

//    dodo to  chyba powinno w agregacie pozostac prawdziwa wartoscia a w read modelu dodac losowa
    private fun getValidLocationOrRandom(ruleValidation: List<RuleValidation>, location: Location): Location {
        val isLocationInsufficientData = ruleValidation.firstOrNull { it.type == IS_WITHIN_TOLERANCE_RANGE }?.result == INSUFFICIENT_DATA
        return if (isLocationInsufficientData) {
            positionCalculator.randomLocationOnCircle(location, 50.0)
        } else {
            location
        }
    }
}