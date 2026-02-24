package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.domain.model.vo.Size
import com.mbuzarewicz.inoapp.domain.model.vo.SizeUnit
import com.mbuzarewicz.inoapp.view.model.BackgroundMapOptionView
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView
import com.mbuzarewicz.inoapp.view.model.SizeView

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
                fileSize = mapToSizeView(fileSize),
                zoomsSize = zoomsSize.mapValues { mapToSizeView(it.value) },
                minZoom = minZoom,
                maxZoom = maxZoom,
                northEast = northEast.let { listOf(it.lat, it.lng) },
                southWest = southWest.let { listOf(it.lat, it.lng) }
            )
        }
    }

    private fun mapToSizeView(size: Size): SizeView {
        return SizeView(
            defaultUnit = "mb",
            values = SizeUnit.entries.associate { unit ->
                unit.name.lowercase() to size.convertUnit(unit).value
            },
        )
    }
}