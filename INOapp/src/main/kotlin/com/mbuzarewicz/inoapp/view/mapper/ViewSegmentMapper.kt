package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.RunTrackSegment
import com.mbuzarewicz.inoapp.view.model.RunTrackSegmentView

class ViewSegmentMapper {
    private val viewGeoMapper = ViewGeoMapper()

    fun mapToView(segment: RunTrackSegment): RunTrackSegmentView {
        return with(segment) {
            RunTrackSegmentView(
                startPoint = viewGeoMapper.mapToView(startPoint),
                endPoint = viewGeoMapper.mapToView(endPoint),
                velocity = velocity
            )
        }
    }
}