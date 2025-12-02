package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.persistence.model.PersistableRoute

class PersistableRouteMapper {
    private val stationMapper = PersistableStationMapper()

    fun mapToPersistableEntity(domain: Route): PersistableRoute {
        return with(domain) {
            PersistableRoute(
                id = id,
                name = name,
                stations = stations.map { stationMapper.mapToPersistableEntity(it) },
                backgroundMapId = backgroundMapId,
                competitionId = competitionId,
                active = isActive,
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRoute): Route {
        return with(persistable) {
            Route(
                id = id,
                name = name,
                stations = stations.map { stationMapper.mapToDomainEntity(it) },
                backgroundMapId = backgroundMapId,
                competitionId = competitionId,
                isActive = active,
            )
        }
    }
}