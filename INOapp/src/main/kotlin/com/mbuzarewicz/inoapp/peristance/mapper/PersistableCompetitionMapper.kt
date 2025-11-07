package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.Competition
import com.mbuzarewicz.inoapp.peristance.model.PersistableCompetition

class PersistableCompetitionMapper {

    fun mapToPersistableEntity(domain: Competition): PersistableCompetition {
        return with(domain) {
            PersistableCompetition(
                id = id,
                name = name,
                adminPasswordHash = adminPasswordHash
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableCompetition): Competition {
        return with(persistable) {
            Competition(
                id = id,
                name = name,
                adminPasswordHash = adminPasswordHash
            )
        }
    }
}