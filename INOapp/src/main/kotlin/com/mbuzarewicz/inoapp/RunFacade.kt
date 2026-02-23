package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.command.CancelRunCommand
import com.mbuzarewicz.inoapp.command.InitiateRunCommand
import com.mbuzarewicz.inoapp.domain.model.Run
import com.mbuzarewicz.inoapp.persistence.repository.DefaultRunRepository
import com.mbuzarewicz.inoapp.view.model.InitiateRunResponse
import com.mbuzarewicz.inoapp.view.model.RunMetricAfterControlPoint
import org.springframework.stereotype.Service

@Service
class RunFacade(
    private val runRepository: DefaultRunRepository,
    private val runReadModelFacade: RunReadModelFacade,
    private val categoryFacade: CategoryFacade,
) {

    fun initiateRun(command: InitiateRunCommand): InitiateRunResponse {
//        dodo nałożyć na wszystko jeszcze warstwe competition, bo teraz wszystkie trasy sa wrzucane do jednego worka, a nie powinno tak byc
//        dodo troche kupa z exception

        val category = categoryFacade.getById(command.categoryId) ?: throw Exception()

        val (run, event) = Run.initiate(
            participantName = command.participantName,
            participantUnit = command.participantUnit,
            categoryId = command.categoryId,
            competitionId = command.competitionId,
        )
        runRepository.saveRun(run)
        runReadModelFacade.createIfNotExistOnRunInitializedEvent(event)

        return InitiateRunResponse(
            runId = run.id,
            backgroundMapId = category.backgroundMapId,
            categoryName = category.name
        )
    }

    fun addControlPoint(command: AddControlPointCommand): RunMetricAfterControlPoint {
        val run = runRepository.getByRunId(command.runId)

        run ?: throw Exception()

        val categoryId = run.categoryId
        val stations = categoryFacade.getStationsByCategoryId(categoryId)
        command.stations.addAll(stations)

        val event = run.addControlPoint(command)
        run.let { runRepository.saveRun(run) }

//        dodo cos tu architektonicznie jest nie tak
//        dodo jak ControlPoint jest nie z tej Route to co wtedy ? Exp z tlumaczeniem co poszlo nie tak ?
        event ?: throw Exception()
        return runReadModelFacade.updateOnAddedControlPointEvent(event)
    }

    fun cancelRun(command: CancelRunCommand) {
        val run = runRepository.getByRunId(command.runId)

        run ?: throw Exception()

        val event = run.cancel()
        run.let { runRepository.saveRun(run) }

        runReadModelFacade.updateOnCanceledRunEvent(event)
    }
}