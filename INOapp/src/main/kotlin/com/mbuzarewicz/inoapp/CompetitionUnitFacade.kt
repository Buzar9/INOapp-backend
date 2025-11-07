package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AddCompetitionUnitCommand
import com.mbuzarewicz.inoapp.command.DeleteCompetitionUnitCommand
import com.mbuzarewicz.inoapp.command.EditCompetitionUnitCommand
import com.mbuzarewicz.inoapp.domain.model.CompetitionUnit
import com.mbuzarewicz.inoapp.peristance.repository.DefaultCompetitionUnitRepository
import com.mbuzarewicz.inoapp.query.GetAllCompetitionUnitQuery
import com.mbuzarewicz.inoapp.view.CompetitionUnitView
import com.mbuzarewicz.inoapp.view.mapper.CompetitionUnitViewMapper
import org.springframework.stereotype.Service
import java.util.*

@Service
class CompetitionUnitFacade(
    private val repository: DefaultCompetitionUnitRepository
) {
    private val viewMapper = CompetitionUnitViewMapper()

    fun add(command: AddCompetitionUnitCommand) {
        val competitionUnit = CompetitionUnit(
            id = UUID.randomUUID().toString(),
            name = command.name,
            competitionId = command.competitionId
        )

        repository.save(competitionUnit)
    }

    fun edit(command: EditCompetitionUnitCommand) {
        val competitionUnit = repository.findById(command.id)

        if (competitionUnit == null) throw Exception()

        val updatedCompetitionUnit = competitionUnit.copy(
            name = command.name
        )
        repository.save(updatedCompetitionUnit)
    }

    fun delete(command: DeleteCompetitionUnitCommand) {
        repository.delete(command.id)
    }

    fun getAllForCompetition(query: GetAllCompetitionUnitQuery): List<CompetitionUnitView> {
        val competitionUnits = repository.getAllByCompetitionId(query.competitionId)
        return competitionUnits.map { viewMapper.mapToView(it) }
    }

    fun getAllForCompetition(competitionId: String): List<CompetitionUnit> {
        return repository.getAllByCompetitionId(competitionId)
    }
}