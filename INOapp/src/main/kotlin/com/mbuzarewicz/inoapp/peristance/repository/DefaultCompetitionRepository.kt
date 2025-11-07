package com.mbuzarewicz.inoapp.peristance.repository

import com.mbuzarewicz.inoapp.domain.model.Competition
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableCompetitionMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultCompetitionRepository(
    private val repository: FirestoreCompetitionRepository
) {
    private val mapper = PersistableCompetitionMapper()

    fun save(competition: Competition) {
        val persistableCompetition = mapper.mapToPersistableEntity(competition)
        repository.save(persistableCompetition)
    }

    fun findByName(name: String): Competition {
        val competition = repository.findByName(name)
        return mapper.mapToDomainEntity(competition)
    }

    fun findAll(): List<Competition> {
        val competitions = repository.findAll()
        return competitions.map { mapper.mapToDomainEntity(it) }
    }
}