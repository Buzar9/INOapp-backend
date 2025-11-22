package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.TranslateService
import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.view.model.ConsolidatedStationView
import com.mbuzarewicz.inoapp.view.model.GeometryView

class ViewConsolidatedStationMapper {

    fun mapToView(route: Route, station: Station): ConsolidatedStationView {
        return ConsolidatedStationView(
            id = station.id,
            name = station.name,
            type = TranslateService.translate("station-type", station.type.toString()),
            note = station.note,
            accuracy = station.location.accuracy,
            isMounted = station.isMounted,
            geometry = GeometryView(
                coordinates = listOf(station.location.lng, station.location.lat)
            ),
            routeId = route.id,
            routeName = route.name
        )
    }
}