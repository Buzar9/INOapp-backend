package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.ControlPoint
import com.mbuzarewicz.inoapp.domain.model.StationType
import com.mbuzarewicz.inoapp.peristance.model.PersistableControlPoint

class PersistableControlPointMapper {
    private val ruleValidationtMapper = PersistableRuleValidationtMapper()
    private val locationMapper = PersistableLocationMapper()

    fun mapToPersistableEntity(domain: ControlPoint): PersistableControlPoint {
        return with(domain) {
            PersistableControlPoint(
                stationId,
                name,
                type.toString(),
                locationMapper.mapToPersistableEntity(location),
                timestamp,
                ruleValidation.map { ruleValidationtMapper.mapToPersistableEntity(it) }
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableControlPoint): ControlPoint {
        return with(persistable) {
            ControlPoint(
                stationId,
                name,
                StationType.valueOf(type),
                locationMapper.mapToDomainEntity(location),
                timestamp,
                ruleValidations.map { ruleValidationtMapper.mapToDomainEntity(it) }
            )
        }
    }
}