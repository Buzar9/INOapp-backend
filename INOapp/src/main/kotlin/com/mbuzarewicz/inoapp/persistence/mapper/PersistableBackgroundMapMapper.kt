package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.persistence.model.PersistableBackgroundMap

class PersistableBackgroundMapMapper {

    fun mapToPersistableEntity(domain: BackgroundMap): PersistableBackgroundMap {
        return with(domain) {
            PersistableBackgroundMap(
                id = id,
                name = name,
                fileSize = fileSize,
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = PersistableLocationMapper().mapToPersistableEntity(northEast),
                southWest = PersistableLocationMapper().mapToPersistableEntity(southWest),
                active = isActive,
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableBackgroundMap): BackgroundMap {
        return with(persistable) {
            BackgroundMap(
                id = id,
                name = name,
                fileSize = fileSize,
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = PersistableLocationMapper().mapToDomainEntity(northEast),
                southWest = PersistableLocationMapper().mapToDomainEntity(southWest),
                isActive = active,
            )
        }
    }
}