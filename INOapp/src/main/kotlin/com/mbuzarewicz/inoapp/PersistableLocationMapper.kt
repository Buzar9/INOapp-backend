package com.mbuzarewicz.inoapp

class PersistableLocationMapper {

    fun mapToPersistableEntity(domain: Location): PersistableLocation {
        return with(domain) {
            PersistableLocation(
                lat = lat,
                lng = lng,
                accuracy = accuracy
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableLocation): Location {
        return with(persistable) {
            Location(
                lat = lat,
                lng = lng,
                accuracy = accuracy
            )
        }
    }
}