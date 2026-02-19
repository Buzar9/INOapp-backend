package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.RunFacade
import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.command.CancelRunCommand
import com.mbuzarewicz.inoapp.view.model.RunMetricAfterControlPoint
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/backoffice/runs"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class BackofficeRunController(
    private val runFacade: RunFacade,
) {

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
        val reporter: String,
        val timestamp: String? = null,
    )

    private fun AddControlPointRequest.toCommand() =
        AddControlPointCommand(runId, stationId, null, timestamp?.toLong(), reporter, mutableListOf())

    @PostMapping(value = ["/cancel"])
    @ResponseStatus(HttpStatus.OK)
    fun cancel(@RequestBody request: CancelRunRequest) {
        runFacade.cancelRun(request.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CancelRunRequest(
        val runId: String,
        val reporter: String
    )

    private fun CancelRunRequest.toCommand() = CancelRunCommand(runId, reporter)
}