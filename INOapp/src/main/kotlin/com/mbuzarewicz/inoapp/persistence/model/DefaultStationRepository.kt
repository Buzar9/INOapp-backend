package com.mbuzarewicz.inoapp.persistence.model

import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.persistence.mapper.PersistableStationMapper
import com.mbuzarewicz.inoapp.persistence.repository.FirestoreStationRepository
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