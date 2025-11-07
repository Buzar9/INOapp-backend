package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CompetitionUnitFacade
import com.mbuzarewicz.inoapp.command.AddCompetitionUnitCommand
import com.mbuzarewicz.inoapp.command.DeleteCompetitionUnitCommand
import com.mbuzarewicz.inoapp.command.EditCompetitionUnitCommand
import com.mbuzarewicz.inoapp.query.GetAllCompetitionUnitQuery
import com.mbuzarewicz.inoapp.view.CompetitionUnitView
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/backoffice/units"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class BackofficeCompetitionUnitController(
    private val competitionUnitFacade: CompetitionUnitFacade,
) {

    @PostMapping(value = ["/add"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun add(
        @RequestBody request: AddCompetitionUnitRequest
    ) {
        competitionUnitFacade.add(request.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddCompetitionUnitRequest(
        val competitionId: String = "Competition123",
        val name: String,
    )

    private fun AddCompetitionUnitRequest.toCommand() = AddCompetitionUnitCommand(competitionId, name)

    @PostMapping(value = ["/edit"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun edit(
        @RequestBody request: EditCompetitionUnitRequest
    ) {
        competitionUnitFacade.edit(request.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EditCompetitionUnitRequest(
        val id: String,
        val name: String
    )

    private fun EditCompetitionUnitRequest.toCommand() = EditCompetitionUnitCommand(id, name)

    @PostMapping(value = ["/delete"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(
        @RequestBody request: DeleteCompetitionUnitRequest
    ) {
        competitionUnitFacade.delete(request.toCommand())
//        dodo obsluga bledow i zwrot odpowieniego http status
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteCompetitionUnitRequest(
        val id: String
    )

    private fun DeleteCompetitionUnitRequest.toCommand() = DeleteCompetitionUnitCommand(id)

    @GetMapping
    fun getAll(): ResponseEntity<List<CompetitionUnitView>> {
//        dodo mock
        val competitionUnits = competitionUnitFacade.getAllForCompetition(GetAllCompetitionUnitQuery("Competition123"))
        return ResponseEntity.status(200).body(competitionUnits)
    }

    data class GetAllCompetitionUnitRequest(
        val competitionId: String,
    )

    private fun GetAllCompetitionUnitRequest.toQuery() = GetAllCompetitionUnitQuery(competitionId)
}