package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.persistence.model.PersistableBackgroundMap

class PersistableBackgroundMapMapper {

    private val sizeMapper = PersistableSizeMapper()

    fun mapToPersistableEntity(domain: BackgroundMap): PersistableBackgroundMap {
        return with(domain) {
            PersistableBackgroundMap(
                id = id,
                name = name,
                competitionId = competitionId,
                fileSize = sizeMapper.mapToPersistableValue(fileSize),
                zoomsSize = zoomsSize.map { (zoom, size) ->
                    zoom.toString() to sizeMapper.mapToPersistableValue(size)
                }.toMap(),
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
                competitionId = competitionId,
                fileSize = sizeMapper.mapToDomainEntity(fileSize),
                zoomsSize = zoomsSize.map { (zoom, size) ->
                    zoom.toInt() to sizeMapper.mapToDomainEntity(size)
                }.toMap(),
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = PersistableLocationMapper().mapToDomainEntity(northEast),
                southWest = PersistableLocationMapper().mapToDomainEntity(southWest),
                isActive = active,
            )
        }
    }
}
