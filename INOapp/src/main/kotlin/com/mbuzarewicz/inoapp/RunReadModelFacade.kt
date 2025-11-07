package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.RunStatus.Companion.isAfterActivation
import com.mbuzarewicz.inoapp.domain.model.StationType.CHECKPOINT
import com.mbuzarewicz.inoapp.event.*
import com.mbuzarewicz.inoapp.peristance.repository.DefaultRunReadModelRepository
import com.mbuzarewicz.inoapp.view.model.RunMetricAfterControlPoint
import org.springframework.stereotype.Component

@Component
class RunReadModelFacade(
    private val repository: DefaultRunReadModelRepository,
    private val categoryFacade: CategoryFacade,
) {
    private val mapper = ScanTrustValidationResultRunReadModelMapper()

    fun getByRunId(runId: String): RunReadModel? {
        return repository.getByRunId(runId)
    }

    fun createIfNotExistOnRunInitializedEvent(event: RunInitiatedEvent) {
        val runReadModel =
            RunReadModel(
                id = event.runId,
                categoryId = event.categoryId,
                competitionId = event.competitionId,
                controlPoints = event.controlPoints,
                participantName = event.participantName,
                participantUnit = event.participantUnit,
                status = event.status,
            )

        repository.save(runReadModel)
    }

    fun updateOnAddedControlPointEvent(event: AddedControlPointEvent): RunMetricAfterControlPoint {
        val runReadModel = repository.getByRunId(event.runId)

        val updatedRunReadModel = when (event) {
            is RunStartedEvent -> {
                runReadModel?.copy(
                    startTime = event.startTime,
                    status = event.status,
                    controlPoints = event.controlPoints
                )
            }

            is AddedCheckpointEvent -> {
                runReadModel?.copy(
                    controlPoints = event.controlPoints
                )
            }

            is RunFinishedEvent -> {
                runReadModel?.copy(
                    controlPoints = event.controlPoints,
                    status = event.status,
                    finishTime = event.finishTime,
                    mainTime = event.mainTime
                )
            }
        }

        updatedRunReadModel?.let { repository.save(it) }

        return RunMetricAfterControlPoint(
            startTime = updatedRunReadModel?.startTime,
            finishTime = updatedRunReadModel?.finishTime,
            mainTime = updatedRunReadModel?.mainTime,
            checkpointsNumber = updatedRunReadModel?.getCheckpointsNumber() ?: 0,
            wasActivate = updatedRunReadModel?.status?.isAfterActivation() ?: false,
            isFinished = updatedRunReadModel?.status == RunStatus.FINISHED,
        )
    }

    fun getAll(): List<RunReadModel> {
//        dodo nałożenie warstwy competitionId
        return repository.getAll()
    }

    fun getFiltered(competitionUnits: List<String>, categories: List<String>, statues: List<String>): List<RunReadModel> {
        val categoriesId =
            if (categories.isNotEmpty()) {
                categoryFacade.getByNames(categories)?.map { it.id } ?: emptyList()
            } else {
                emptyList()
            }
        return repository.getFiltered(competitionUnits, categoriesId, statues)
    }

    private fun RunReadModel.getCheckpointsNumber() = controlPoints.filter { it.type == CHECKPOINT }.size
}