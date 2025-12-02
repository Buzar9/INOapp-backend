package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.RunTrack
import com.mbuzarewicz.inoapp.persistence.model.PersistableRunTrack

class PersistableRunTrackMapper {
    private val pointMapper = PersistableRunTrackPointMapper()

    fun mapToPersistableEntity(domain: RunTrack): PersistableRunTrack {
        return with(domain) {
            PersistableRunTrack(
                id = id,
                points = points.map { pointMapper.mapToPersistableEntity(it) }
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRunTrack): RunTrack {
        return with(persistable) {
            RunTrack(
                id = id,
                points = points.map { pointMapper.mapToDomainEntity(it) }
            )
        }
    }
}

