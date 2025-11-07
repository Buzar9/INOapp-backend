package com.mbuzarewicz.inoapp.controler

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CompetitionFacade
import com.mbuzarewicz.inoapp.RaceResultViewFacade
import com.mbuzarewicz.inoapp.command.CreateCompetitionCommand
import com.mbuzarewicz.inoapp.view.model.RaceResultView
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
class SecurityController(
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

//     dodo
//    @GetMapping("/competition/results")
//    fun getResults(): ResponseEntity<List<RaceResultView>> {
//        val results = raceResultViewFacade.getResults()
//        return ResponseEntity.status(200).body(results)
//    }
}