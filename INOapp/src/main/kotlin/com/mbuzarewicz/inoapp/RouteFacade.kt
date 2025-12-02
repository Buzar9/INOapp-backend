package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.*
import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.domain.model.StationType
import com.mbuzarewicz.inoapp.persistence.repository.DefaultRouteRepository
import com.mbuzarewicz.inoapp.query.GetConsolidatedRouteViewQuery
import com.mbuzarewicz.inoapp.query.GetAllRoutesQuery
import com.mbuzarewicz.inoapp.query.GetRouteQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewConsolidatedStationMapper
import com.mbuzarewicz.inoapp.view.mapper.ViewRouteMapper
import com.mbuzarewicz.inoapp.view.model.ConsolidatedRouteView
import com.mbuzarewicz.inoapp.view.model.RouteOptionView
import com.mbuzarewicz.inoapp.view.model.RouteView
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RequestMapping(path = ["/backoffice/routes"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
@Component
class RouteFacade(
    private val routeRepository: DefaultRouteRepository,
    private val backgroundMapFacade: BackgroundMapFacade,
    private val viewConsolidatedStationMapper: ViewConsolidatedStationMapper = ViewConsolidatedStationMapper()
) {
    private val routeMapper = ViewRouteMapper()

    fun addRoute(command: AddRouteCommand): RouteView {
        val routeId = UUID.randomUUID().toString()
        val route = Route(
            id = routeId,
            name = command.routeName,
            stations = emptyList(),
            backgroundMapId = command.backgroundMapId,
            competitionId = command.competitionId,
            isActive = true
        )

        routeRepository.save(route)
        val backgroundMap = backgroundMapFacade.getById(command.backgroundMapId)!!

        return routeMapper.mapToView(route, backgroundMap)
    }

    fun editRoute(command: EditRouteCommand): RouteView {
        val route = routeRepository.getById(command.routeId)

        if (route == null) throw Exception("dodo")

        val updatedRoute = route.copy(name = command.name)
        routeRepository.save(updatedRoute)
        val backgroundMap = backgroundMapFacade.getById(updatedRoute.backgroundMapId)!!

        return routeMapper.mapToView(updatedRoute, backgroundMap)
    }

    fun deactivate(command: DeleteRouteCommand): List<RouteView> {
        val route = routeRepository.getById(command.routeId)
        if (route == null) throw Exception("dodo")

        val updatedRoute = route.copy(isActive = false)
        routeRepository.save(updatedRoute)

        return getAllView(command.competitionId)
    }

    fun addStation(command: AddStationCommand): RouteView {
        val route = routeRepository.getById(command.routeId)

        if (route == null) throw Exception("dodo")

        val station = Station(
            id = UUID.randomUUID().toString(),
            name = command.name,
            type = StationType.valueOf(command.type),
            location = command.location,
            note = command.note
        )

        val updatedRoute = route.copy(stations = route.stations + station)
        routeRepository.save(updatedRoute)
        val backgroundMap = backgroundMapFacade.getById(updatedRoute.backgroundMapId)!!

        return routeMapper.mapToView(updatedRoute, backgroundMap)
    }

    fun editStation(command: EditStationCommand): RouteView {
        val route = routeRepository.getById(command.routeId)

        if (route == null) throw Exception("dodo")

        val newStation = Station(
            id = command.stationId,
            name = command.name,
            type = StationType.valueOf(command.type),
            location = command.location,
            note = command.note
        )

        val updatedStations = route.stations.map { if (it.id == newStation.id) newStation else it }

        val updatedRoute = route.copy(stations = updatedStations)
        routeRepository.save(updatedRoute)
        val backgroundMap = backgroundMapFacade.getById(updatedRoute.backgroundMapId)!!

        return routeMapper.mapToView(updatedRoute, backgroundMap)
    }

    fun deleteStation(command: DeleteStationCommand): RouteView {
        val route = routeRepository.getById(command.routeId)

        if (route == null) throw Exception("dodo")

        val updatedStations = route.stations.filterNot { it.id == command.stationId }

        val updatedRoute = route.copy(stations = updatedStations)
        routeRepository.save(updatedRoute)
        val backgroundMap = backgroundMapFacade.getById(updatedRoute.backgroundMapId)!!

        return routeMapper.mapToView(updatedRoute, backgroundMap)
    }

    fun toggleStationMount(command: ToggleStationMountCommand): RouteView {
        val route = routeRepository.getById(command.routeId)

        if (route == null) throw Exception("dodo")

        val updatedStations = route.stations.map { station ->
            if (station.id == command.stationId) {
                station.copy(isMounted = !station.isMounted)
            } else {
                station
            }
        }

        val updatedRoute = route.copy(stations = updatedStations)
        routeRepository.save(updatedRoute)
        val backgroundMap = backgroundMapFacade.getById(updatedRoute.backgroundMapId)!!

        return routeMapper.mapToView(updatedRoute, backgroundMap)
    }

    fun getAllView(query: GetAllRoutesQuery): List<RouteView> {
        return getAllView(query.competitionId)
    }

    fun getRouteOptions(query: GetAllRoutesQuery): List<RouteOptionView> {
        val routes = routeRepository.getAll(query.competitionId)
        return routes.filter { it.isActive }.map { routeMapper.mapToOptionView(it) }
    }

    fun getRoute(id: String): Route? {
        return routeRepository.getById(id)
    }

    fun getRouteView(query: GetRouteQuery): RouteView? {
//        dodo wywalić wszystkie !! zewsząd z calej aplikacji
        val route = routeRepository.getById(query.routeId)!!
        val backgroundMap = backgroundMapFacade.getById(route.backgroundMapId)!!

        return route.let { routeMapper.mapToView(route, backgroundMap) }
    }

    fun getConsolidatedStationView(query: GetConsolidatedRouteViewQuery): ConsolidatedRouteView {
        val routes = routeRepository.getAllActive(query.competitionId)
        val consolidatedStations = routes.flatMap { route ->
            route.stations.map { station ->
                viewConsolidatedStationMapper.mapToView(route, station)
            }
        }

        return ConsolidatedRouteView(
            consolidatedStations = consolidatedStations
        )
    }

    private fun getAllView(competitionId: String): List<RouteView> {
        val routes = routeRepository.getAll(competitionId)

        return routes.filter { it.isActive }
            .map {
                routeMapper.mapToView(it, backgroundMapFacade.getById(it.backgroundMapId)!!)
            }
    }
}