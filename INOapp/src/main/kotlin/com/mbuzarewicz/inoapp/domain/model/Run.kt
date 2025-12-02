package com.mbuzarewicz.inoapp.domain.model

import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.RunStatus.*
import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.domain.model.StationType.*
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
    val stations: MutableList<Station>,
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

    companion object {
        fun initiate(
            participantName: String,
            participantUnit: String,
            categoryId: String,
            competitionId: String,
            stations: List<Station>
        ): Pair<Run, RunInitiatedEvent> {
            val runId = UUID.randomUUID().toString()
            return Pair(
                Run(
                    id = runId,
                    categoryId = categoryId,
                    competitionId = competitionId,
                    stations = stations.toMutableList(),
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
            stations: List<Station>,
            controlPoints: MutableList<ControlPoint>,
            startTime: Long?,
            finishTime: Long?,
            status: RunStatus
        ): Run {
            val run = Run(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
                stations = stations.toMutableList(),
                controlPoints = controlPoints,
                status = status
            )
            run.startTime = startTime
            run.finishTime = finishTime

            return run
        }
    }

    fun addControlPoint(command: AddControlPointCommand): AddedControlPointEvent? {
        val station = stations.firstOrNull { it.id == command.stationId }

        if (station == null) return null

        return when (station.type) {
            START_RUN -> {
                start(command.stationId, command.location, command.timestamp)
            }

            CHECKPOINT -> {
                addCheckpoint(command.stationId, command.location, command.timestamp)
            }

            FINISH_RUN -> {
                finish(command.stationId, command.location, command.timestamp)
            }
        }
    }

//    dodo nie wiem czy nie kupa? czy mozna odpytywac agregatu o jego stan ?
    fun getMainTime(): Long {
        if (startTime == null || finishTime == null) throw Exception("dodo")

        return finishTime!! - startTime!!
    }

    private fun start(stationId: String, location: Location, timestamp: Long): RunStartedEvent? {
        if (status != INITIATED) return null

        val station = stations.firstOrNull { it.id == stationId }

        if (station == null || station.type != START_RUN) return null

        val ruleValidation = startRuleValidator.validate(location, station.location)
//        dodo co z tym mapperem i co z trzymaniem wynikow walidacji w agregacie?

        val controlPoint = ControlPoint(
            stationId = stationId,
            name = station.name,
            type = station.type,
            location = location,
            timestamp = timestamp,
            ruleValidation = ruleValidation
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

    private fun addCheckpoint(stationId: String, location: Location, timestamp: Long): AddedCheckpointEvent? {
        if (status != STARTED) return null
        if (controlPoints.any { it.stationId == stationId }) return null

        val station = stations.firstOrNull { it.id == stationId }

        if (station == null || station.type != CHECKPOINT) return null

        val lastControlStationTimestamp = controlPoints.maxByOrNull { it.timestamp }?.timestamp
        val ruleValidation = controlPointRuleValidator.validate(
            lastControlStationTimestamp,
            timestamp,
            location,
            station.location
        )

        val controlPoint = ControlPoint(
            stationId = stationId,
            name = station.name,
            type = station.type,
            location = location,
            timestamp = timestamp,
            ruleValidation = ruleValidation
        )
        controlPoints.add(controlPoint)

        return AddedCheckpointEvent(
            runId = id,
            controlPoints = controlPoints,
        )
    }

    private fun finish(stationId: String, location: Location, timestamp: Long): RunFinishedEvent? {
        if (status != STARTED) return null

        val station = stations.firstOrNull { it.id == stationId }

        if (station == null || station.type != FINISH_RUN) return null

        val ruleValidation =
            finishRuleValidator.validate(location, station.location, startTime!!, timestamp)

        status = FINISHED

        finishTime = timestamp

        val controlPoint = ControlPoint(
            stationId = stationId,
            name = station.name,
            type = station.type,
            location = location,
            timestamp = timestamp,
            ruleValidation = ruleValidation
        )
        controlPoints.add(controlPoint)

        return RunFinishedEvent(
            runId = id,
            controlPoints = controlPoints,
            status = status,
            finishTime = timestamp,
            mainTime = getMainTime(),
        )
    }
}