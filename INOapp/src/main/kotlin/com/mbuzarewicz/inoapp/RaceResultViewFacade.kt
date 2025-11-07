package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.TimePresenter.Companion.formatToAbsoluteHoursMinutesSeconds
import com.mbuzarewicz.inoapp.TimePresenter.Companion.formatToDailyHour
import com.mbuzarewicz.inoapp.query.GetFilteredCompetitionResultsQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewBackgroundMapMapper
import com.mbuzarewicz.inoapp.view.mapper.ViewControlPointViewMapper
import com.mbuzarewicz.inoapp.view.model.*
import org.springframework.stereotype.Component

//dodo czy to rozdzielać na fasade i factory?
@Component
class RaceResultViewFacade(
    private val runReadModelFacade: RunReadModelFacade,
    private val categoryFacade: CategoryFacade,
    private val backgroundMapFacade: BackgroundMapFacade,
) {
    private val viewControlPointMapper = ViewControlPointViewMapper()
    private val viewBackgroundMapMapper = ViewBackgroundMapMapper()

    fun getResults(query: GetFilteredCompetitionResultsQuery): List<RaceResultView> {
        val runReadModels = runReadModelFacade.getAll()

        val categories = query.filter?.get("category") ?: emptyList()
        val competitionUnits = query.filter?.get("team") ?: emptyList()
        val statues = query.filter?.get("status") ?: emptyList()

        val dodo = runReadModelFacade.getFiltered(categories = categories, competitionUnits = competitionUnits, statues = statues)

//        dodo mapper do view ? jak jest gdzie indziej ?
//        dodo zaoszczędzić strzałów, może zrobić categoryReadModel, który ma w sobie backgroundMap i route?
        val results = dodo.map {
            val category = categoryFacade.getById(it.categoryId) ?: throw Exception()
            val startTimeToDisplay = formatToDailyHour(it.startTime)
            val finishTimeToDisplay = formatToDailyHour(it.finishTime)
            val mainTimeToDisplay = formatToAbsoluteHoursMinutesSeconds(it.mainTime)

//            dodo jak zrobic edytowalną polityke wyswietlania wynikow, od gory zwycięscy?
            RaceResultView(
                participantNickname = it.participantName,
                participantUnit = it.participantUnit,
                categoryName = category.name,
                routeId = category.routeId,
                status = TranslateService.translate("run-status", it.status.toString()),
                startTime = startTimeToDisplay,
                finishTime = finishTimeToDisplay,
                mainTime = mainTimeToDisplay,
                controlPoints = it.controlPoints.map { controlPoint -> viewControlPointMapper.mapToView(controlPoint) }
                    .sortedWith(compareBy { controlPoint -> controlPoint.timestamp })
            )
        }

//        DODO mock
        val resultsDodo = defaultRaceResultViews()
//        val resultsDodo = emptyList<RaceResultView>()

//          dodo mock
        return (results + resultsDodo).sortedWith(
            compareByDescending<RaceResultView> { it.controlPoints.size }.thenBy { it.mainTime }
        )
    }

    private fun defaultRaceResultViews(): List<RaceResultView> {
        val resultsDodo = listOf(
            RaceResultView(
                "Dodo participiant",
                "Dodo unit",
                "T-Extreme",
                "43bd99b3-d5f7-419a-92da-0cb937a721bb",
                "Zakończony",
                "10:00",
                "13:30",
                "3:30",
                listOf(
                    ControlPointView(
                        "stationId0",
                        "Start",
                        "START_RUN",
                        "10:00",
                        listOf(
                            RuleValidationView(
                                "Odległość w granicach tolerancji",
                                "INSUFFICIENT_DATA"
                            )
                        ),
                        GeoView(
                            geometry = GeoGeometryView(coordinates = listOf(0.0, 0.0)),
                            properties = mapOf(
                                "accuracy" to "70.0"
                            )
                        )
                    ),
                    ControlPointView(
                        "stationId1",
                        "1",
                        "CHECKPOINT",
                        "10:16",
                        listOf(
                            RuleValidationView(
                                "Odległość w granicach tolerancji",
                                "PASSED"
                            ),
                            RuleValidationView(
                                "Odstęp skanów odpowiednio długi",
                                "PASSED"
                            )
                        ),
                        GeoView(
                            geometry = GeoGeometryView(coordinates = listOf(15.7276, 51.9705)),
                            properties = mapOf(
                                "accuracy" to "50.0"
                            )
                        )
                    ),
                    ControlPointView(
                        "stationIdx",
                        "Meta",
                        "FINISH_RUN",
                        "13:30",
                        listOf(
                            RuleValidationView(
                                "Odległość w granicach tolerancji",
                                "FAILED"
                            ),
                            RuleValidationView(
                                "Skończył w czasie",
                                "PASSED"
                            )
                        ),
                        GeoView(
                            geometry = GeoGeometryView(coordinates = listOf(15.7256, 51.9664)),
                            properties = mapOf(
                                "accuracy" to "70.0"
                            )
                        )
                    )
                )
            ),
            RaceResultView(
                "Dodo participiant 2",
                "Dodo unit",
                "Topór-Extreme",
                "43bd99b3-d5f7-419a-92da-0cb937a721bb",
                "Rozpoczęty",
                "10:00",
                "---",
                "---",
                listOf(
                    ControlPointView(
                        "stationId1",
                        "start",
                        "START_RUN",
                        "10:00",
                        listOf(
                            RuleValidationView(
                                "Odległość w granicach tolerancji",
                                "PASSED"
                            )
                        ),
                        GeoView(
                            geometry = GeoGeometryView(coordinates = listOf(15.6257, 51.9549)),
                            properties = mapOf(
                                "accuracy" to "70.0"
                            )
                        )
                    ),
                    ControlPointView(
                        "stationId1",
                        "1",
                        "CHECKPOINT",
                        "10:16",
                        listOf(
                            RuleValidationView(
                                "Odległość w granicach tolerancji",
                                "PASSED"
                            ),
                            RuleValidationView(
                                "Odstęp skanów odpowiednio długi",
                                "PASSED"
                            )
                        ),
                        GeoView(
                            geometry = GeoGeometryView(coordinates = listOf(15.7279, 51.9707)),
                            properties = mapOf(
                                "accuracy" to "50.0"
                            )
                        )
                    ),
                )
            )
        )
        return resultsDodo
    }
//
//    private fun mapToValidationStationResultViewList(
//        validationMap: Map<CheckpointId, List<ValidationResult>>,
//        controlPoints: List<ControlPoint>
//    ): List<ValidationStationResultView> {
//        val resultList = mutableListOf<ValidationStationResultView>()
//
//        validationMap.forEach { (checkpointId, validationResults) ->
//            validationResults.forEach { validationResult ->
//                val status = when (validationResult) {
//                    is ValidationResult.Pass -> "Pass"
//                    is ValidationResult.Fail -> "Fail"
//                    is ValidationResult.InsufficientData -> "InsufficientData"
//                }
//
////                dodo może w readmodelu timestamp powinien byc przyklejony do resultatu validacji?
//                val view = ValidationStationResultView(
//                    checkpointId = checkpointId.toStringId(),
//                    type = translator.translate("station-validation-type", validationResult.type.toString()),
//                    status = status,
//                    details = validationResult.details.mapToString(),
//                )
//                resultList.add(view)
//            }
//        }
//        return resultList
//    }
//
//    private fun Map<String, String>.mapToString(): String {
//        return this.entries.joinToString(", ") { (key, value) -> "$key: $value" }
//    }
}