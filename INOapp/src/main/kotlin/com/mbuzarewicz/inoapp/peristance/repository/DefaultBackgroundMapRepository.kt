package com.mbuzarewicz.inoapp.peristance.repository

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableBackgroundMapMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultBackgroundMapRepository(
    private val repository: FirestoreBackgroundMapRepository
) {
    private val mapper = PersistableBackgroundMapMapper()

    fun getByIdIn(backgroundMapIds: List<String>): List<BackgroundMap> {
        val persistableBackgroundMaps = repository.getByIdIn(backgroundMapIds)
        return persistableBackgroundMaps.map { mapper.mapToDomainEntity(it) }
    }

    fun getById(backgroundMapId: String): BackgroundMap? {
        val persistableBackgroundMap = repository.getById(backgroundMapId) ?: return null
        return mapper.mapToDomainEntity(persistableBackgroundMap)
    }

    fun getAll(competitionId: String): List<BackgroundMap> {
//        dodo dodac warstwe competitionId w BackgroundMap
        val persistableBackgroundMaps = repository.getAll()
        return persistableBackgroundMaps.map { mapper.mapToDomainEntity(it) }
    }

    fun save(backgroundMap: BackgroundMap) {
        val persistableRoute = mapper.mapToPersistableEntity(backgroundMap)
        repository.save(persistableRoute)
    }
}