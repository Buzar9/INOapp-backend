package com.mbuzarewicz.inoapp

class PersistablePenaltyMapper {

    fun mapToPersistableEntity(domain: Penalty): PersistablePenalty {
        return with(domain) {
            PersistablePenalty(
                id = id,
                timePenalty = timePenalty,
                cause = cause,
                details = details
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistablePenalty): Penalty {
        return with(persistable) {
            Penalty(
                id = id,
                timePenalty = timePenalty,
                cause = cause!!,
                details = details
            )
        }
    }
}