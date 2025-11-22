package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.TranslateService
import com.mbuzarewicz.inoapp.domain.model.ControlPoint
import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.view.model.GeometryView
import com.mbuzarewicz.inoapp.view.model.GeoView

class ViewGeoMapper {

    fun mapToView(station: Station): GeoView {
        return with(station) {
            GeoView(
                geometry = GeometryView(
                    coordinates = listOf(location.lng, location.lat)
                ),
//                zrezygnowac z properties i wrzucic to po bozemu do osobnych pol
//                dodo tutaj mozna to rozdzielic na obiekt z informacjami i obiekt do wyswietlenia na mapie tak jak w controlPoint
                properties = mapOf(
                    "id" to id,
                    "name" to name,
                    "type" to TranslateService.translate("station-type", type.toString()),
                    "note" to note,
                    "accuracy" to location.accuracy.toString(),
                    "isMounted" to isMounted.toString()
                )
            )
        }
    }

    fun mapToView(controlPoint: ControlPoint): GeoView {
        return with(controlPoint) {
            GeoView(
                geometry = GeometryView(
                    coordinates = listOf(location.lng, location.lat)
                ),
                properties = mapOf(
                    "name" to name,
                    "accuracy" to location.accuracy.toString(),
                    "timestamp" to timestamp.toString()
                )
            )
        }
    }
}