package com.mbuzarewicz.inoapp.controler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CompetitionFacade
import com.mbuzarewicz.inoapp.RaceResultViewFacade
import com.mbuzarewicz.inoapp.command.CreateCompetitionCommand
import com.mbuzarewicz.inoapp.query.GetFilteredCompetitionResultsQuery
import com.mbuzarewicz.inoapp.view.model.RaceResultView
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/backoffice"], produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["http://localhost:4200"])
class CompetitionController(
    private val competitionFacade: CompetitionFacade,
    private val raceResultViewFacade: RaceResultViewFacade
) {

    @PostMapping("/create")
    fun create(@RequestBody request: CreateCompetitionRequest): ResponseEntity<String> {
//        dodo obsluga tworzenia nowego Competiton
//        if (repo.findByName(req.name) != null) return ResponseEntity.badRequest().body("Contest exists")
        val competitionId = competitionFacade.create(request.toCommand())
        return ResponseEntity.status(200).body(competitionId)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CreateCompetitionRequest(
        val signature: String,
        val adminPassword: String
    )

    private fun CreateCompetitionRequest.toCommand() =
        CreateCompetitionCommand(name = signature, adminPassword = adminPassword)

    @PostMapping("/competition/results")
    fun getResults(@RequestBody request: GetResultsRequest): ResponseEntity<List<RaceResultView>> {
        val query = request.toQuery()
        val results = raceResultViewFacade.getResults(query)
        return ResponseEntity.status(200).body(results)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GetResultsRequest(
        val filter: Map<String, List<String>>?,
        val pageNumber: Long
    )

    private fun GetResultsRequest.toQuery() =
        GetFilteredCompetitionResultsQuery(filter, pageNumber)
}