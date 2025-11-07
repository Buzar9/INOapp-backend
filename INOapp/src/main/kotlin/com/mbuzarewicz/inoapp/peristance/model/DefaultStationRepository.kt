package com.mbuzarewicz.inoapp.peristance.model

import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableStationMapper
import com.mbuzarewicz.inoapp.peristance.repository.FirestoreStationRepository
import org.springframework.stereotype.Repository

@Repository
class DefaultStationRepository(
    private val repository: FirestoreStationRepository
) {
    private val mapper = PersistableStationMapper()

    fun save(station: Station) {
        val persistable = mapper.mapToPersistablePatternEntity(station)
        repository.save(persistable)
    }

    fun findAll(): List<Station> {
        val persistable = repository.findAll()
        return persistable.map { mapper.mapToDomainEntity(it) }
    }

//    fun getByRouteName(routeName: String): List<RouteStations> {
//        val persistableConstantStation = repository.getByRouteName(routeName)
//        return persistableConstantStation.map { mapper.mapToDomainEntity(it) }
//    }
}