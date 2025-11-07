package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.view.model.BackgroundMapOptionView
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView

class ViewBackgroundMapMapper {

    fun mapToOptionView(backgroundMap: BackgroundMap): BackgroundMapOptionView {
        return with(backgroundMap) {
            BackgroundMapOptionView(
                id = id,
                name = name
            )
        }
    }

    fun mapToView(backgroundMap: BackgroundMap): BackgroundMapView {
        return with(backgroundMap) {
            BackgroundMapView(
                id = id,
                name = name,
                fileUrl = fileUrl,
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = northEast.let { listOf(it.lat, it.lng) },
                southWest = southWest.let { listOf(it.lat, it.lng) }
            )
        }
    }
}