package com.mbuzarewicz.inoapp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/runs"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class RunController(
    private val runFacade: RunFacade,
//    dodo osobny kontroler
    private val raceResultViewFacade: RaceResultViewFacade
) {

    @PostMapping(value = ["/initiate"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun initiateRun(
        @RequestBody initiateRunRequest: InitiateRunRequest
    ) {
        runFacade.initiateRun(initiateRunRequest.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @PostMapping(value = ["/start"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun startRun(
        @RequestBody startRunRequest: StartRunRequest
    ) {
        runFacade.startRun(startRunRequest.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @PostMapping(value = ["/add_checkpoint"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addCheckpoint(
        @RequestBody addCheckpointRequest: AddCheckpointRequest
    ) {
        runFacade.addCheckpoint(addCheckpointRequest.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @PostMapping(value = ["/finish"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun finishRun(
        @RequestBody finishRunRequest: FinishRunRequest
    ) {
        runFacade.finishRun(finishRunRequest.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @PostMapping(value = ["/add_penalty"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addPenalty(
        @RequestBody addPenaltyRequest: AddPenaltyRequest
    ) {
        runFacade.addPenalty(addPenaltyRequest.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @GetMapping(value = ["/race_results"])
    @ResponseStatus(HttpStatus.OK)
    fun getRaceResults(): List<RaceResultView> {
        return raceResultViewFacade.getResults()
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class InitiateRunRequest(
        val runId: String,
        val routeName: String,
        val competitionCategory: String,
        val nickname: String,
        val team: String,
    )

    private fun InitiateRunRequest.toCommand() =
        InitiateRunCommand(runId, routeName, competitionCategory, nickname, team)

    //    dodo co z pustą lokalizacją?
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class StartRunRequest(
        val runId: String,
        val location: Location,
        val timestamp: Long?,
    )

    private fun StartRunRequest.toCommand() =
        StartRunCommand(runId, location, timestamp)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddCheckpointRequest(
        val runId: String,
        val checkpointId: String,
        val routeName: String,
        val location: Location,
        val timestamp: Long,
    )

    private fun AddCheckpointRequest.toCommand() =
        AddCheckpointCommand(runId, checkpointId, routeName, location, timestamp)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class FinishRunRequest(
        val runId: String,
        val location: Location,
        val timestamp: Long,
    )

    private fun FinishRunRequest.toCommand() =
        FinishRunCommand(runId, location, timestamp)

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddPenaltyRequest(
        val runId: String,
        val penaltyId: String,
        val offenseValue: String,
        val cause: PenaltyCause,
        val timestamp: Long,
    )

    private fun AddPenaltyRequest.toCommand() =
        AddPenaltyCommand(runId, penaltyId, offenseValue, cause, timestamp)
}