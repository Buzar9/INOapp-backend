package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Component

@Component
class RunReadModelFacade(
    private val repository: DefaultRunReadModelRepository
) {
    private val mapper = ScanTrustValidationResultRunReadModelMapper()

    //    dodo kupa
    fun createIfNotExistOnRunInitializedEvent(event: RunInitiatedEvent) {
        val runReadModel =
            RunReadModel(
                runId = event.runId,
                nickname = event.nickname,
                team = event.team,
                routeName = event.routeName,
                competitionCategory = event.competitionCategory,
                status = event.status,
                startTime = event.startTime,
                finishTime = event.finishTime,
                mainTime = event.mainTime,
                totalTime = event.totalTime,
                visitedCheckpointsNumber = event.visitedCheckpoints.size,
                visitedCheckpoints = event.visitedCheckpoints,
                stationsValidationResults = event.stationsValidationResults,
                penalties = event.penalties
            )

        repository.saveIfNotExist(runReadModel)
    }

    fun updateOnRunStartedEvent(event: RunStartedEvent) {
        val runReadModel = repository.getByRunId(event.runId)
        val updatedRunReadModel = runReadModel?.copy(
            stationsValidationResults =
                event.stationsValidationResults.mapValues { (_, value) -> value.map { mapper.map(it) } }
                    .toMutableMap(),
            penalties = event.penalties,
            startTime = event.startTime,
            status = event.status
        )

        updatedRunReadModel?.let { repository.save(it) }
    }

    fun updateOnAddedCheckpointEvent(event: AddedCheckpointEvent) {
        val runReadModel = repository.getByRunId(event.runId)
        val updatedRunReadModel = runReadModel?.copy(
            visitedCheckpointsNumber = event.visitedCheckpoints.size,
            visitedCheckpoints = event.visitedCheckpoints.toMutableList(),
            stationsValidationResults =
                event.stationsValidationResults.mapValues { (_, value) -> value.map { mapper.map(it) } }
                    .toMutableMap(),
        )

        updatedRunReadModel?.let { repository.save(it) }
    }

    fun updateOnRunFinishedEvent(event: RunFinishedEvent) {
        val runReadModel = repository.getByRunId(event.runId)
        val updatedRunReadModel = runReadModel?.copy(
            status = event.status,
            finishTime = event.finishTime,
            mainTime = event.mainTime,
            totalTime = event.totalTime,
            stationsValidationResults =
                event.stationsValidationResults.mapValues { (_, value) -> value.map { mapper.map(it) } }
                    .toMutableMap(),
        )

        updatedRunReadModel?.let { repository.save(it) }
    }

//    dodo fun penalties

    fun getAll(): List<RunReadModel> {
        return repository.getAll()
    }
}