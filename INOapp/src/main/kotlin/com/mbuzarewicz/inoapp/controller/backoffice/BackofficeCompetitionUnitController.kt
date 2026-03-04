package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CompetitionUnitFacade
import com.mbuzarewicz.inoapp.command.AddCompetitionUnitCommand
import com.mbuzarewicz.inoapp.command.DeleteCompetitionUnitCommand
import com.mbuzarewicz.inoapp.command.EditCompetitionUnitCommand
import com.mbuzarewicz.inoapp.query.GetAllCompetitionUnitQuery
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
        @RequestHeader("X-Competition-Id") competitionId: String,
        @RequestBody request: AddCompetitionUnitRequest
    ) {
        competitionUnitFacade.add(request.toCommand(competitionId))
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddCompetitionUnitRequest(
        val name: String,
    )

    private fun AddCompetitionUnitRequest.toCommand(competitionId: String) = AddCompetitionUnitCommand(competitionId, name)

    @PostMapping(value = ["/edit"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun edit(
        @RequestBody request: EditCompetitionUnitRequest
    ) {
        competitionUnitFacade.edit(request.toCommand())
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
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteCompetitionUnitRequest(
        val id: String
    )

    private fun DeleteCompetitionUnitRequest.toCommand() = DeleteCompetitionUnitCommand(id)

    @GetMapping
    fun getAll(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<*> {
        val competitionUnits = competitionUnitFacade.getAllForCompetition(GetAllCompetitionUnitQuery(competitionId))
        return ResponseEntity.status(200).body(competitionUnits)
    }
}
