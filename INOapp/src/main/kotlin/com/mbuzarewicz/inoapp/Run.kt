package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.PenaltyCause.SCANNED_START_IN_ANOTHER_LOCATION
import com.mbuzarewicz.inoapp.RunStatus.*
import com.mbuzarewicz.inoapp.StationType.FINISH_RUN
import com.mbuzarewicz.inoapp.StationType.START_RUN
import com.mbuzarewicz.inoapp.StationValidationType.RUNNING_TIME_EXCEEDED_LIMIT
import com.mbuzarewicz.inoapp.StationValidationType.SCAN_LOCATION_IS_TOO_FAR
import com.mbuzarewicz.inoapp.ValidationResult.Fail
import java.util.*

class Run(
//    dodo jak wylaniac zwyciescow? poki co idzie sortowana lista
    val runId: String,
    val routeName: String,
    val competitionCategory: String,
    val constantStations: List<ConstantStation>,
    val timePenaltyPolicy: TimePenaltyPolicy,
    val visitedCheckpoints: MutableList<Checkpoint> = mutableListOf(),
//    dodo czy to w ogole jest potrzebne w agregacie?
    val stationsValidationResults: MutableMap<CheckpointId, List<ValidationResult>> = mutableMapOf(),
    val penalties: MutableList<Penalty> = mutableListOf(),
    var startTime: Long = 0,
    var finishTime: Long = 0,
    var status: RunStatus = GENERATED,
) {
    private val startTrustValidator = StartTrustValidator()
    private val checkpointTrustValidator = CheckpointTrustValidator()
    private val finishTrustValidator = FinishTrustValidator()
    private val scanTrustValidationMapper = ScanTrustValidationResultRunMapper()

    fun initiateRun(command: InitiateRunCommand): RunInitiatedEvent? {
        if (status != GENERATED) return null
        status = INITIATED

        return RunInitiatedEvent(
            runId = runId,
            nickname = command.nickname,
            team = command.team,
            routeName = routeName,
            competitionCategory = competitionCategory,
            status = status,
            startTime = startTime,
            finishTime = finishTime,
            mainTime = calculateMainTime(),
            totalTime = calculateTotalTime(),
            visitedCheckpointsNumber = visitedCheckpoints.size,
            visitedCheckpoints = visitedCheckpoints,
            stationsValidationResults = stationsValidationResults,
            penalties = penalties
        )
    }

    fun startRun(command: StartRunCommand): RunStartedEvent? {
        if (status != INITIATED) return null

//        dodo sypie sie jak nie znajdzie odpowiedniej trasy
        val constantStation = constantStations.first { it.type == START_RUN }
        val scanTrustValidationResults = startTrustValidator.validate(command.location, constantStation.location)
        val validationResults = scanTrustValidationResults.map { scanTrustValidationMapper.map(it) }


//        dodo kupa
        val checkpointId = CheckpointId("start", routeName)
        stationsValidationResults[checkpointId] = validationResults

        val isScannedAnotherInAnotherLocation =
            validationResults.any { it is Fail && it.type == SCAN_LOCATION_IS_TOO_FAR }

        if (isScannedAnotherInAnotherLocation) {
            val cause = SCANNED_START_IN_ANOTHER_LOCATION
            val timePenalty = timePenaltyPolicy.calculatePenalty(cause, routeName)

            penalties.add(
                Penalty(
                    id = UUID.randomUUID().toString(),
                    timePenalty = timePenalty,
                    cause = cause,
//                    dodo kupa
//                    details = validationResult.first { it is Fail && it.type == SCAN_LOCATION_IS_TOO_FAR }.details.toString()
                    details = "validationResult.first { it is Fail && it.type == SCAN_LOCATION_IS_TOO_FAR }.details.toString()"
                )
            )
        }

//        dodo pułapka bo dajesz domyslna date startu
        startTime = command.timestamp ?: (System.currentTimeMillis() / 1000)
        status = STARTED

//        dodo jak zrobic logi z validacja w readmodelu. zeby mozna bylo sobie wszystko ladnie po kolei zobaczyc
        return RunStartedEvent(runId, mapOf(checkpointId to scanTrustValidationResults), penalties, startTime, status)
    }

    fun addCheckpoint(command: AddCheckpointCommand): AddedCheckpointEvent? {
        if (isDuplicate(command.checkpointId, command.routeName)) return null
        if (isRouteNameNOTMatching(command.routeName)) return null
        if (status != STARTED) return null

        val checkpointId = CheckpointId(command.checkpointId, command.routeName)
        val checkpoint = Checkpoint(checkpointId, command.location, command.timestamp)
        val lastCheckpointTimestamp = visitedCheckpoints.maxByOrNull { it.timestamp }?.timestamp
        val constantCheckpoint =
            constantStations.first { it.stationId == command.checkpointId }
//        dodo co z pustym location?
        val scanTrustValidationResults = checkpointTrustValidator.validate(
            lastCheckpointTimestamp,
            command.timestamp,
            command.location,
            constantCheckpoint.location
        )
        val validationResults = scanTrustValidationResults.map { scanTrustValidationMapper.map(it) }

        visitedCheckpoints.add(checkpoint)
        stationsValidationResults[checkpointId] = validationResults

        return AddedCheckpointEvent(
            runId = runId,
            visitedCheckpoints = visitedCheckpoints,
            stationsValidationResults = mapOf(checkpointId to scanTrustValidationResults)
        )
    }

    fun finishRun(command: FinishRunCommand): RunFinishedEvent? {
        if (status != STARTED) return null

        val constantCheckpoint =
            constantStations.first { it.type == FINISH_RUN }
        val scanTrustValidationResults =
            finishTrustValidator.validate(command.location, constantCheckpoint.location, startTime, command.timestamp)
        val validationResult = scanTrustValidationResults.map { scanTrustValidationMapper.map(it) }

//        dodo troche kupa
        val checkpointId = CheckpointId("finish", routeName)
        stationsValidationResults[checkpointId] = validationResult

        val isDisqualifiedByTime =
            validationResult.first { it.type == RUNNING_TIME_EXCEEDED_LIMIT } is Fail

        status = if (isDisqualifiedByTime) DISQUALIFIED_BY_TIME else FINISHED

        finishTime = command.timestamp

        return RunFinishedEvent(
            runId = runId,
            status = status,
            finishTime = finishTime,
            mainTime = calculateMainTime(),
            totalTime = calculateTotalTime(),
            stationsValidationResults = mapOf(checkpointId to scanTrustValidationResults)
        )
    }

    fun addPenalty(command: AddPenaltyCommand) {
        if (isDuplicate(command.penaltyId)) return

        val timePenalty = timePenaltyPolicy.calculatePenalty(command.cause, command.offenseValue)

        val penalty = with(command) { Penalty(penaltyId, timePenalty, cause) }

        penalties.add(penalty)
//        dodo event
    }

    private fun calculateMainTime() = finishTime - startTime

    private fun calculateTotalTime(): Long {
        val mainTime = calculateMainTime()
        val penaltiesTime = penalties.sumOf { it.timePenalty }
        return mainTime + penaltiesTime
    }

    private fun isDuplicate(id: String, routeName: String) =
        visitedCheckpoints.any { it.checkpointId == CheckpointId(id, routeName) }

    private fun isRouteNameNOTMatching(checkpointRouteName: String) = routeName != checkpointRouteName

    private fun isDuplicate(id: String) = penalties.any { it.id == id }
}