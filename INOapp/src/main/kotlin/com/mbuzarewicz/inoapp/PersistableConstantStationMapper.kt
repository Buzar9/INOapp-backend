package com.mbuzarewicz.inoapp

class PersistableConstantStationMapper {
    private val locationMapper = PersistableLocationMapper()

    fun mapToPersistableEntity(domain: ConstantStation): PersistableConstantStation {
        return with(domain) {
            PersistableConstantStation(
//                id = databaseId,
                stationId = stationId,
//                routeName = routeName,
                location = locationMapper.mapToPersistableEntity(location),
                type = type.toString()
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableConstantStation): ConstantStation {
        return with(persistable) {
            ConstantStation(
//                databaseId = id,
                stationId = stationId,
//                routeName = routeName,
                location = locationMapper.mapToDomainEntity(location),
                type = StationType.valueOf(type)
            )
        }
    }
}