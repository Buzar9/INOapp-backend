package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.Category
import com.mbuzarewicz.inoapp.persistence.model.PersistableCategory

class PersistableCategoryMapper {

    fun mapToPersistableEntity(domain: Category): PersistableCategory {
        return with(domain) {
            PersistableCategory(
                id = id,
                name = name,
                competitionId = competitionId,
                routeId = routeId,
                maxTime = maxTime,
                backgroundMapId = backgroundMapId
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableCategory): Category {
        return with(persistable) {
            Category(
                id = id,
                name = name,
                competitionId = competitionId,
                routeId = routeId,
                maxTime = maxTime,
                backgroundMapId = backgroundMapId
            )
        }
    }
}