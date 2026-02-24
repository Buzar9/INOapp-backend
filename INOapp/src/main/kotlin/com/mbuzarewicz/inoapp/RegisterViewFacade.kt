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
        val competitionIds = competitions.map { it.id }

        val unitsByCompetition = unitFacade.getAllForCompetitions(competitionIds)
        val competitionUnits = unitsByCompetition.mapValues { (_, units) -> units.map { it.name } }

        return RegisterView(
            competitions = competitions.associate { it.id to it.name },
            competitionsUnits = competitionUnits
        )
    }
}