package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.domain.model.StationType
import com.mbuzarewicz.inoapp.peristance.model.PersistablePatternStation
import com.mbuzarewicz.inoapp.peristance.model.PersistableStation

class PersistableStationMapper {
    private val locationMapper = PersistableLocationMapper()

    fun mapToPersistableEntity(domain: Station): PersistableStation {
        return with(domain) {
            PersistableStation(
                id = id,
                name = name,
                type = type.toString(),
                location = locationMapper.mapToPersistableEntity(location),
                note = note,
                mounted = isMounted
            )
        }
    }

    fun mapToPersistablePatternEntity(domain: Station): PersistablePatternStation {
        return with(domain) {
            PersistablePatternStation(
                id = id,
                name = name,
                type = type.toString(),
                location = locationMapper.mapToPersistableEntity(location),
                note = note,
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableStation): Station {
        return with(persistable) {
            Station(
                id = id,
                name = name,
                type = StationType.valueOf(type),
                location = locationMapper.mapToDomainEntity(location),
                note = note,
                isMounted = mounted
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistablePatternStation): Station {
        return with(persistable) {
            Station(
                id = id,
                name = name,
                type = StationType.valueOf(type),
                location = locationMapper.mapToDomainEntity(location),
                note = note,
            )
        }
    }
}