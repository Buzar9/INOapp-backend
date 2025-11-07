package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.peristance.model.PersistableLocation

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