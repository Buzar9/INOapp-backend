package com.mbuzarewicz.inoapp.persistence.repository

import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.persistence.mapper.PersistableRouteMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultRouteRepository(
    private val repository: FirestoreRouteRepository
) {
    private val mapper = PersistableRouteMapper()

    fun getById(id: String): Route? {
        val persistableRoute = repository.findActiveById(id)
        return persistableRoute?.let { mapper.mapToDomainEntity(persistableRoute) }
    }

    fun getAllActive(competitionId: String): List<Route> {
        val persistableRoutes = repository.getAllActive(competitionId)
        return persistableRoutes.map { mapper.mapToDomainEntity(it) }
    }

    fun getByIds(routeIds: List<String>): List<Route> {
        val persistableRoutes = repository.getActiveByIds(routeIds)
        return persistableRoutes.map { mapper.mapToDomainEntity(it) }
    }

    fun save(route: Route) {
        val persistableRoute = mapper.mapToPersistableEntity(route)
        repository.save(persistableRoute)
    }
}