package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Repository

@Repository
class DefaultConstantStationRepository(
    private val repository: FirestoreConstantStationRepository
) {
    private val mapper = PersistableConstantStationMapper()

    fun getByRouteName(routeName: String): List<ConstantStation> {
        val persistableConstantStation = repository.getByRouteName(routeName)
        return persistableConstantStation.map { mapper.mapToDomainEntity(it) }
    }
}