package com.mbuzarewicz.inoapp.controler.participant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.RegisterViewFacade
import com.mbuzarewicz.inoapp.RunFacade
import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.command.InitiateRunCommand
import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.view.model.InitiateRunResponse
import com.mbuzarewicz.inoapp.view.model.RunMetricAfterControlPoint
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/runs"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class ParticipantRunController(
    private val registerViewFacade: RegisterViewFacade,
    private val runFacade: RunFacade,
) {

    @PostMapping(value = ["/initiate"])
    @ResponseStatus(HttpStatus.OK)
    fun initiateRun(
        @RequestBody initiateRunRequest: InitiateRunRequest
    ): ResponseEntity<InitiateRunResponse> {
        val initiateRunResponse = runFacade.initiateRun(initiateRunRequest.toCommand())
        return ResponseEntity.status(200).body(initiateRunResponse)
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class InitiateRunRequest(
        val competitionId: String,
        val categoryId: String,
        val participantName: String,
        val participantUnitName: String,
    )

    private fun InitiateRunRequest.toCommand() =
        InitiateRunCommand(categoryId, participantName, participantUnitName, competitionId)

    @PostMapping(value = ["/add_control_point"])
    @ResponseStatus(HttpStatus.OK)
    fun addControlPoint(
        @RequestBody request: AddControlPointRequest
    ): ResponseEntity<RunMetricAfterControlPoint> {
//        dodo przy podwojnym zeskanowaniu wywala NPE albo przy zeskanowaniu punktu kontrolnego nie ze swojej trasy
        val runMetricAfterControlPoint = runFacade.addControlPoint(request.toCommand())
        return ResponseEntity.status(200).body(runMetricAfterControlPoint)
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddControlPointRequest(
        val runId: String,
        val stationId: String,
        val location: Location,
        val timestamp: String
    )

    private fun AddControlPointRequest.toCommand() =
        AddControlPointCommand(runId, stationId, location, timestamp.toLong())

//    @PostMapping(value = ["/accept"])
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    fun acceptRun(
//        @RequestBody acceptRunRequest: AcceptRunRequest
//    ) {
//        runFacade.acceptRun(acceptRunRequest.toCommand())
////        dodo obsluga bledow i zwrot odpowieniego http status
//    }

//    @JsonIgnoreProperties(ignoreUnknown = true)
//    data class AcceptRunRequest(
//        val runId: String
//    )
//
//    private fun AcceptRunRequest.toCommand() =
//        AcceptRunCommand(runId)

//    @PostMapping(value = ["/change_category"])
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    fun acceptRun(
//        @RequestBody acceptRunRequest: ChangeRunCategoryRequest
//    ) {
//        runFacade.changeRunCategory(acceptRunRequest.toCommand())
////        dodo obsluga bledow i zwrot odpowieniego http status
//    }

//    @JsonIgnoreProperties(ignoreUnknown = true)
//    data class ChangeRunCategoryRequest(
//        val runId: String,
//        val categoryId: String,
//    )
//
//    private fun ChangeRunCategoryRequest.toCommand() =
//        ChangeRunCategoryCommand(runId = runId, categoryId = categoryId)

//    @PostMapping(value = ["/start"])
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    fun startRun(
//        @RequestBody startRunRequest: StartRunRequest
//    ) {
//        runFacade.startRun(startRunRequest.toCommand())
////        dodo obsluga bledow i zwrot odpowieniego http status
//    }
//
//    //    dodo co z pustą lokalizacją?
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    data class StartRunRequest(
//        val runId: String,
//        val stationId: String,
//        val location: Location,
//        val timestamp: Long,
//    )
//
//    private fun StartRunRequest.toCommand() =
//        StartRunCommand(runId, stationId, location, timestamp)

//    @PostMapping(value = ["/add_checkpoint"])
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    fun addCheckpoint(
//        @RequestBody addCheckpointRequest: AddCheckpointRequest
//    ) {
//        runFacade.addCheckpoint(addCheckpointRequest.toCommand())
////        dodo obsluga bledow i zwrot odpowieniego http status
//    }
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    data class AddCheckpointRequest(
//        val runId: String,
//        val checkpointId: String,
//        val location: Location,
//        val timestamp: Long,
//    )
//
//    private fun AddCheckpointRequest.toCommand() =
//        AddCheckpointCommand(runId, checkpointId, location, timestamp)

//    @PostMapping(value = ["/finish"])
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    fun finishRun(
//        @RequestBody finishRunRequest: FinishRunRequest
//    ) {
//        runFacade.finishRun(finishRunRequest.toCommand())
////        dodo obsluga bledow i zwrot odpowieniego http status
//    }
//
//    @JsonIgnoreProperties(ignoreUnknown = true)
//    data class FinishRunRequest(
//        val runId: String,
//        val stationId: String,
//        val location: Location,
//        val timestamp: Long,
//    )
//
//    private fun FinishRunRequest.toCommand() =
//        FinishRunCommand(runId, stationId, location, timestamp)

}