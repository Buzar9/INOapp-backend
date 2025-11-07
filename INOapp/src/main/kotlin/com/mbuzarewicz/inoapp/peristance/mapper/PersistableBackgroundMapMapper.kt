package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.peristance.model.PersistableBackgroundMap

class PersistableBackgroundMapMapper {

    fun mapToPersistableEntity(domain: BackgroundMap): PersistableBackgroundMap {
        return with(domain) {
            PersistableBackgroundMap(
                id = id,
                name = name,
                fileUrl = fileUrl,
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = PersistableLocationMapper().mapToPersistableEntity(northEast),
                southWest = PersistableLocationMapper().mapToPersistableEntity(southWest),
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableBackgroundMap): BackgroundMap {
        return with(persistable) {
            BackgroundMap(
                id = id,
                name = name,
                fileUrl = fileUrl,
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = PersistableLocationMapper().mapToDomainEntity(northEast),
                southWest = PersistableLocationMapper().mapToDomainEntity(southWest),
            )
        }
    }
}