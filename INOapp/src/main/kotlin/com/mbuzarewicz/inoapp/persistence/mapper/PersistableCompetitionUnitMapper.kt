package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.CompetitionUnit
import com.mbuzarewicz.inoapp.persistence.model.PersistableCompetitionUnit

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