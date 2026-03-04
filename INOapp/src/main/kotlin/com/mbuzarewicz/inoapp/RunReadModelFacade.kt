package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.RunStatus.Companion.isAfterActivation
import com.mbuzarewicz.inoapp.domain.model.Category
import com.mbuzarewicz.inoapp.domain.model.StationType.CHECKPOINT
import com.mbuzarewicz.inoapp.event.*
import com.mbuzarewicz.inoapp.persistence.repository.DefaultRunReadModelRepository
import com.mbuzarewicz.inoapp.view.mapper.ViewControlPointViewMapper
import com.mbuzarewicz.inoapp.view.model.RunMetricAfterControlPoint
import org.springframework.stereotype.Component

@Component
class RunReadModelFacade(
    private val repository: DefaultRunReadModelRepository,
    private val categoryFacade: CategoryFacade,
) {
    private val viewControlPointMapper = ViewControlPointViewMapper()

    fun createIfNotExistOnRunInitializedEvent(event: RunInitiatedEvent, category: Category) {
        val runReadModel =
            RunReadModel(
                id = event.runId,
                categoryId = event.categoryId,
                competitionId = event.competitionId,
                controlPoints = event.controlPoints,
                participantName = event.participantName,
                participantUnit = event.participantUnit,
                status = event.status,
                categoryName = category.name,
                categoryRouteId = category.routeId,
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

        val controlPoints = updatedRunReadModel?.controlPoints
            ?.map { controlPoint -> viewControlPointMapper.mapToView(controlPoint) }
            ?: emptyList()

        return RunMetricAfterControlPoint(
            startTime = updatedRunReadModel?.startTime,
            finishTime = updatedRunReadModel?.finishTime,
            mainTime = updatedRunReadModel?.mainTime,
            controlPoints = controlPoints,
            checkpointsNumber = updatedRunReadModel?.getCheckpointsNumber() ?: 0,
            wasActivate = updatedRunReadModel?.status?.isAfterActivation() ?: false,
            isFinished = updatedRunReadModel?.status == RunStatus.FINISHED,
        )
    }

    fun getAll(competitionId: String): List<RunReadModel> {
        return repository.getAll(competitionId)
    }

    fun updateOnCanceledRunEvent(event: RunCanceledEvent) {
        val runReadModel = repository.getByRunId(event.runId)

        val updatedRunReadModel = runReadModel?.copy(
            status = event.status
        )

        updatedRunReadModel?.let { repository.save(it) }
    }

    private fun RunReadModel.getCheckpointsNumber() = controlPoints.filter { it.type == CHECKPOINT }.size
}