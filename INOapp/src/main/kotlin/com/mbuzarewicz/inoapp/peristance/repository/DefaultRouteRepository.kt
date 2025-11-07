package com.mbuzarewicz.inoapp.peristance.repository

import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableRouteMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultRouteRepository(
    private val repository: FirestoreRouteRepository
) {
    private val mapper = PersistableRouteMapper()

    fun getById(id: String): Route? {
        val persistableRoute = repository.findById(id)
        return persistableRoute?.let { mapper.mapToDomainEntity(persistableRoute) }
    }

    fun getByName(routeName: String): Route? {
        val persistableRoute = repository.getByRouteName(routeName)
        return persistableRoute?.let { mapper.mapToDomainEntity(persistableRoute) }
    }

    fun getAll(competitionId: String): List<Route> {
        val persistableRoutes = repository.getAll(competitionId)
        return persistableRoutes.map { mapper.mapToDomainEntity(it) }
    }

    fun save(route: Route) {
        val persistableRoute = mapper.mapToPersistableEntity(route)
        repository.save(persistableRoute)
    }

    fun deleteById(id: String) {
        repository.delete(id)
    }
}