package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Repository

@Repository
class DefaultRouteRepository(
    private val repository: FirestoreRouteRepository
) {
    private val mapper = PersistableRouteMapper()

    fun getByRouteId(routeName: String): Route? {
        val persistableRoute = repository.getByRouteName(routeName)
        return persistableRoute?.let { mapper.mapToDomainEntity(persistableRoute) }
    }

    fun getAll(): List<Route> {
        val persistableRoutes = repository.getAll()
        return persistableRoutes.map { mapper.mapToDomainEntity(it) }
    }

    fun saveRoute(route: Route) {
        val persistableRoute = mapper.mapToPersistableEntity(route)
        repository.save(persistableRoute)
    }
}