package com.mbuzarewicz.inoapp.controler.participant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.RegisterViewFacade
import com.mbuzarewicz.inoapp.RunFacade
import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.command.CancelRunCommand
import com.mbuzarewicz.inoapp.command.InitiateRunCommand
import com.mbuzarewicz.inoapp.domain.model.Location
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
    ): ResponseEntity<*> {
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
    ): ResponseEntity<*> {
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
        AddControlPointCommand(runId, stationId, location, timestamp.toLong(), "USER", mutableListOf())

    @PostMapping(value = ["/cancel"])
    @ResponseStatus(HttpStatus.OK)
    fun cancel(@RequestBody request: CancelRunRequest) {
        runFacade.cancelRun(request.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CancelRunRequest(
        val runId: String
    )

    private fun CancelRunRequest.toCommand() = CancelRunCommand(runId, "USER")
}