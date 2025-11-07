package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.TimePresenter
import com.mbuzarewicz.inoapp.domain.model.ControlPoint
import com.mbuzarewicz.inoapp.view.model.ControlPointView

class ViewControlPointViewMapper {
    private val viewGeoMapper = ViewGeoMapper()
    private val viewRuleValidationMapper = ViewRuleValidationMapper()

    fun mapToView(controlPoint: ControlPoint): ControlPointView {
        return with(controlPoint) {
            ControlPointView(
                stationId,
                name,
                type.toString(),
                TimePresenter.formatToDailyHour(timestamp),
                ruleValidation = ruleValidation.map { viewRuleValidationMapper.mapToView(it) },
                geoView = viewGeoMapper.mapToView(controlPoint)
            )
        }
    }
}