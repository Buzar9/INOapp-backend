package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.RunTrackPoint
import com.mbuzarewicz.inoapp.persistence.model.PersistableRunTrackPoint

class PersistableRunTrackPointMapper {
    private val mapper = PersistableLocationMapper()
    fun mapToPersistableEntity(point: RunTrackPoint): PersistableRunTrackPoint {
        return with(point) {
            PersistableRunTrackPoint(
                timestamp = timestamp,
                location = mapper.mapToPersistableEntity(location)
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRunTrackPoint): RunTrackPoint {
        return with(persistable) {
            RunTrackPoint(
                timestamp = timestamp,
                location = mapper.mapToDomainEntity(location)
            )
        }
    }
}
