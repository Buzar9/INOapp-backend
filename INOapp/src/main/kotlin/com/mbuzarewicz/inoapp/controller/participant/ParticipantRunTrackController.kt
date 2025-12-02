package com.mbuzarewicz.inoapp.controller.participant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.RunTrackFacade
import com.mbuzarewicz.inoapp.command.AppendRunTrackPointsCommand
import com.mbuzarewicz.inoapp.domain.model.RunTrackPoint
import com.mbuzarewicz.inoapp.view.model.RunTrackView
import com.mbuzarewicz.inoapp.query.GetRunTrackQuery
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/run_trucks"], produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["http://localhost:4200"])
class ParticipantRunTrackController(
    private val runTrackFacade: RunTrackFacade,
) {
    @PostMapping("/batch")
    fun uploadRunTrackBatch(
        @RequestBody request: RunTrackBatchRequest
    ): ResponseEntity<Map<String, Any>> {
        return try {
            val result = runTrackFacade.uploadRunTrackBatch(request.toCommand())
            ResponseEntity.ok(result)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(mapOf("error" to (e.message ?: "Unknown error")))
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class RunTrackBatchRequest(
        val runId: String,
        val points: List<RunTrackPoint>
    )

    private fun RunTrackBatchRequest.toCommand() = AppendRunTrackPointsCommand(
        runId = this.runId,
        points = this.points
    )

    @PostMapping
    fun getRunTrack(@RequestBody request: GetRunTrackRequest): ResponseEntity<RunTrackView> {
            val response = runTrackFacade.getRunTrack(request.toQuery())
            return ResponseEntity.ok(response)
    }

    @JsonIgnoreProperties
    data class GetRunTrackRequest(
        val runId: String
    )

    private fun GetRunTrackRequest.toQuery() = GetRunTrackQuery(runId = this.runId)
}
