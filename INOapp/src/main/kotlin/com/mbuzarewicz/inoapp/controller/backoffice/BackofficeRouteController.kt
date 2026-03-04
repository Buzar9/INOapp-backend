package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.RouteFacade
import com.mbuzarewicz.inoapp.command.*
import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.query.GetAllRoutesQuery
import com.mbuzarewicz.inoapp.query.GetConsolidatedRouteViewQuery
import com.mbuzarewicz.inoapp.view.model.ConsolidatedRouteView
import com.mbuzarewicz.inoapp.view.model.RouteOptionView
import com.mbuzarewicz.inoapp.view.model.RouteView
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/backoffice/routes"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class BackofficeRouteController(
    private val routeFacade: RouteFacade,
) {

    @PostMapping(value = ["/create"])
    @ResponseStatus(HttpStatus.OK)
    fun addRoute(
        @RequestHeader("X-Competition-Id") competitionId: String,
        @RequestBody addRouteRequest: AddRouteRequest
    ): ResponseEntity<RouteView> {
        val newRoute = routeFacade.addRoute(addRouteRequest.toCommand(competitionId))
        return ResponseEntity.status(200).body(newRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddRouteRequest(
        val name: String,
        val backgroundMapId: String,
    )

    private fun AddRouteRequest.toCommand(competitionId: String) = AddRouteCommand(name, backgroundMapId, competitionId)

    @PostMapping(value = ["/edit_route"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editRoute(
        @RequestBody request: EditRouteRequest
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeFacade.editRoute(request.toCommand())
        return ResponseEntity.status(200).body(updatedRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EditRouteRequest(
        val routeId: String,
        val name: String,
    )

    private fun EditRouteRequest.toCommand() = EditRouteCommand(routeId, name)

    @PostMapping(value = ["/delete_route"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRoute(
        @RequestHeader("X-Competition-Id") competitionId: String,
        @RequestBody request: DeleteRouteRequest
    ): ResponseEntity<List<RouteView>> {
        val updatedRoute = routeFacade.deactivate(request.toCommand(competitionId))
        return ResponseEntity.status(200).body(updatedRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteRouteRequest(
        val routeId: String,
    )

    private fun DeleteRouteRequest.toCommand(competitionId: String) = DeleteRouteCommand(competitionId, routeId)

//    dodo zrobic walidacje, że może być tylko jeden start i jedna meta
    @PostMapping(value = ["/add_station"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun addStation(
        @RequestBody request: AddStationRequest
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeFacade.addStation(request.toCommand())
        return ResponseEntity.status(200).body(updatedRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddStationRequest(
        val routeId: String,
        val name: String,
        val type: String,
        val location: Location,
//        note moze byc null
        val note: String,
    )

    private fun AddStationRequest.toCommand() = AddStationCommand(routeId, name, type, location, note)

    @PostMapping(value = ["/edit_station"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun editStation(
        @RequestBody request: EditStationRequest
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeFacade.editStation(request.toCommand())
        return ResponseEntity.status(200).body(updatedRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class EditStationRequest(
        val routeId: String,
        val stationId: String,
        val name: String,
        val type: String,
        val location: Location,
        val note: String,
    )

    private fun EditStationRequest.toCommand() = EditStationCommand(routeId, stationId, name, type, location, note)

    //    dodo zmienic na delete
    @PostMapping(value = ["/delete_station"])
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteStation(
        @RequestBody request: DeleteStationRequest
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeFacade.deleteStation(request.toCommand())
        return ResponseEntity.status(200).body(updatedRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteStationRequest(
        val routeId: String,
        val stationId: String,
    )

    private fun DeleteStationRequest.toCommand() = DeleteStationCommand(routeId, stationId)

    @PostMapping(value = ["/toggle_station_mount"])
    @ResponseStatus(HttpStatus.OK)
    fun toggleStationMount(
        @RequestBody request: ToggleStationMountRequest
    ): ResponseEntity<RouteView> {
        val updatedRoute = routeFacade.toggleStationMount(request.toCommand())
        return ResponseEntity.status(200).body(updatedRoute)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ToggleStationMountRequest(
        val routeId: String,
        val stationId: String,
    )

    private fun ToggleStationMountRequest.toCommand() = ToggleStationMountCommand(routeId, stationId)

    @PostMapping
    fun getAll(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<List<RouteView>> {
        val routesViews = routeFacade.getAllView(GetAllRoutesQuery(competitionId))
        return ResponseEntity.status(200).body(routesViews)
    }

    @PostMapping(value = ["/options"])
    fun getRouteOptions(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<List<RouteOptionView>> {
        val routesViews = routeFacade.getRouteOptions(GetAllRoutesQuery(competitionId))
        return ResponseEntity.status(200).body(routesViews)
    }

    @PostMapping(value = ["/consolidated_routes"])
    fun getConsolidatedRouteView(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<ConsolidatedRouteView> {
        val consolidatedRouteView = routeFacade.getConsolidatedStationView(GetConsolidatedRouteViewQuery(competitionId))
        return ResponseEntity.status(200).body(consolidatedRouteView)
    }
}
