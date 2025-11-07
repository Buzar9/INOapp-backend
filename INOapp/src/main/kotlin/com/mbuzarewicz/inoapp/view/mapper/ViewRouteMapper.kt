package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView
import com.mbuzarewicz.inoapp.view.model.RouteOptionView
import com.mbuzarewicz.inoapp.view.model.RouteView

class ViewRouteMapper {
    private val viewGeoMapper = ViewGeoMapper()
    private val viewBackgroundMapMapper = ViewBackgroundMapMapper()

    fun mapToView(route: Route, backgroundMap: BackgroundMap): RouteView {
        return with(route) {
            RouteView(
                id = id,
                name = name,
                stations = stations.map { station -> viewGeoMapper.mapToView(station) }
                    .sortedWith(compareBy { station -> station.properties["name"] }),
                backgroundMap = viewBackgroundMapMapper.mapToView(backgroundMap)
            )
        }
    }

    fun mapToOptionView(route: Route): RouteOptionView {
        return with(route) {
            RouteOptionView(
                id = id,
                name = name
            )
        }
    }
}