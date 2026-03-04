package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.TimePresenter.Companion.formatToAbsoluteHoursMinutesSeconds
import com.mbuzarewicz.inoapp.TimePresenter.Companion.formatToDailyHour
import com.mbuzarewicz.inoapp.query.GetFilteredCompetitionResultsQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewControlPointViewMapper
import com.mbuzarewicz.inoapp.view.model.RaceResultView
import org.springframework.stereotype.Component

@Component
class RaceResultViewFacade(
    private val runReadModelFacade: RunReadModelFacade,
) {
    private val viewControlPointMapper = ViewControlPointViewMapper()

    fun getResults(query: GetFilteredCompetitionResultsQuery): List<RaceResultView> {
//        dodo bez filtrowania bo na firebase jest za drogie
        val runReadModels = runReadModelFacade.getAll(query.competitionId)

//        dodo mapper do view ? jak jest gdzie indziej ?

        val results = runReadModels.map {
            val startTimeToDisplay = formatToDailyHour(it.startTime)
            val finishTimeToDisplay = formatToDailyHour(it.finishTime)
            val mainTimeToDisplay = formatToAbsoluteHoursMinutesSeconds(it.mainTime)

//            dodo jak zrobic edytowalną polityke wyswietlania wynikow, od gory zwycięscy?
            RaceResultView(
                runId = it.id,
                participantNickname = it.participantName,
                participantUnit = it.participantUnit,
                categoryName = it.categoryName,
                routeId = it.categoryRouteId,
                translatedStatus = TranslateService.translate("run-status", it.status.toString()),
                status = it.status.toString(),
                startTime = startTimeToDisplay,
                finishTime = finishTimeToDisplay,
                mainTime = mainTimeToDisplay,
                controlPoints = it.controlPoints.map { controlPoint -> viewControlPointMapper.mapToView(controlPoint) }
                    .sortedWith(compareBy { controlPoint -> controlPoint.timestamp })
            )
        }

//        dodo polityka do wyswietlania wynikow i mozliwosci sortowania
        return results.sortedWith(
            compareByDescending<RaceResultView> { it.controlPoints.size }.thenBy { it.mainTime }
        )
    }
}