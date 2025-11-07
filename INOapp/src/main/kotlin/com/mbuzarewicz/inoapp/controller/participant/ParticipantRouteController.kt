package com.mbuzarewicz.inoapp.controler.participant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.RouteFacade
import com.mbuzarewicz.inoapp.query.GetRouteQuery
import com.mbuzarewicz.inoapp.view.model.RouteView
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/routes"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class ParticipantRouteController(
    private val routeFacade: RouteFacade,
) {

    @PostMapping
    fun getRoute(
        @RequestBody request: GetRouteRequest
    ): ResponseEntity<RouteView> {
        val routeView = routeFacade.getRouteView(request.toQuery())
        return ResponseEntity.status(200).body(routeView)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GetRouteRequest(
        val routeId: String,
    )

    private fun GetRouteRequest.toQuery() = GetRouteQuery(routeId)
}