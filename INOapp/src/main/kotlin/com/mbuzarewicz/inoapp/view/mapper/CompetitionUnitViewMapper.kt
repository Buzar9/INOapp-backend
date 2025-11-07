package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.CompetitionUnit
import com.mbuzarewicz.inoapp.view.CompetitionUnitView

class CompetitionUnitViewMapper {
    fun mapToView(competitionUnit: CompetitionUnit): CompetitionUnitView {
        return CompetitionUnitView(
            id = competitionUnit.id,
            name = competitionUnit.name,
        )
    }
}