package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.view.model.RegisterView
import org.springframework.stereotype.Component

@Component
class RegisterViewFacade(
    private val competitionFacade: CompetitionFacade,
    private val unitFacade: CompetitionUnitFacade
) {

    fun getData(): RegisterView {
        val competitions = competitionFacade.getAll()
        val competitionUnits =
            competitions.associate { competition -> competition.id to getCompetitionUnitsNames(competition.id) }

        return RegisterView(
            competitions = competitions.associate { it.id to it.name },
            competitionsUnits = competitionUnits
        )
    }

    private fun getCompetitionUnitsNames(competitionId: String): List<String> {
        return unitFacade.getAllForCompetition(competitionId).map { it.name }
    }
}