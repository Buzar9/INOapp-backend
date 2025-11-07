package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.CompetitionUnit
import com.mbuzarewicz.inoapp.peristance.model.PersistableCompetitionUnit

class PersistableCompetitionUnitMapper {

    fun mapToPersistableEntity(domain: CompetitionUnit): PersistableCompetitionUnit {
        return with(domain) {
            PersistableCompetitionUnit(
                id = id,
                name = name,
                competitionId = competitionId,
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableCompetitionUnit): CompetitionUnit {
        return with(persistable) {
            CompetitionUnit(
                id = id,
                name = name,
                competitionId = competitionId,
            )
        }
    }
}