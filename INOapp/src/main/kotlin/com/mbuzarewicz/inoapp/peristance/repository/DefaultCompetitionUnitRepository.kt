package com.mbuzarewicz.inoapp.peristance.repository

import com.mbuzarewicz.inoapp.domain.model.CompetitionUnit
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableCompetitionUnitMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultCompetitionUnitRepository(
    private val repository: FirestoreCompetitionUnitRepository
) {
    private val mapper = PersistableCompetitionUnitMapper()

    fun getAllByCompetitionId(competitionId: String): List<CompetitionUnit> {
        val persistableCompetitionUnit = repository.getAll(competitionId)
        return persistableCompetitionUnit.map { mapper.mapToDomainEntity(it) }
    }

    fun findById(id: String): CompetitionUnit? {
        val persistableCompetitionUnit = repository.findById(id)
        return persistableCompetitionUnit?.let { mapper.mapToDomainEntity(it) }
    }

    fun save(competitionUnit: CompetitionUnit) {
        val persistableRoute = mapper.mapToPersistableEntity(competitionUnit)
        repository.save(persistableRoute)
    }

    fun delete(id: String) {
        repository.delete(id)
    }
}