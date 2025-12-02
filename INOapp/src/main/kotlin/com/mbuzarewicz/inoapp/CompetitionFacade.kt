package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.CreateCompetitionCommand
import com.mbuzarewicz.inoapp.domain.model.Competition
import com.mbuzarewicz.inoapp.persistence.repository.DefaultCompetitionRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class CompetitionFacade(
    private val repository: DefaultCompetitionRepository,
) {

    fun create(command: CreateCompetitionCommand): String {
        val competition = Competition(
            id = UUID.randomUUID().toString(),
            name = command.name,
            adminPasswordHash = "dodo mock"
        )
        repository.save(competition)
        return competition.id
    }

    fun getAll(): List<Competition> {
        return repository.findAll()
    }
}