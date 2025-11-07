package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AddControlPointCommand
import com.mbuzarewicz.inoapp.command.InitiateRunCommand
import com.mbuzarewicz.inoapp.domain.model.Run
import com.mbuzarewicz.inoapp.peristance.repository.DefaultRouteRepository
import com.mbuzarewicz.inoapp.peristance.repository.DefaultRunRepository
import com.mbuzarewicz.inoapp.view.model.InitiateRunResponse
import com.mbuzarewicz.inoapp.view.model.RunMetricAfterControlPoint
import org.springframework.stereotype.Service

@Service
class RunFacade(
    private val runRepository: DefaultRunRepository,
    private val routeRepository: DefaultRouteRepository,
    private val runReadModelFacade: RunReadModelFacade,
    private val categoryFacade: CategoryFacade,
) {

    fun initiateRun(command: InitiateRunCommand): InitiateRunResponse {
//        dodo nałożyć na wszystko jeszcze warstwe competition, bo teraz wszystkie trasy sa wrzucane do jednego worka, a nie powinno tak byc
//        dodo troche kupa z exception

        val category = categoryFacade.getById(command.categoryId) ?: throw Exception()
        val route = routeRepository.getById(category.routeId) ?: throw Exception()

        val (run, event) = Run.initiate(
            participantName = command.participantName,
            participantUnit = command.participantUnit,
            categoryId = command.categoryId,
            competitionId = command.competitionId,
            stations = route.stations
        )
        runRepository.saveRun(run)
        runReadModelFacade.createIfNotExistOnRunInitializedEvent(event)

        return InitiateRunResponse(
            runId = run.id,
            backgroundMapId = category.backgroundMapId
        )
    }

    fun addControlPoint(command: AddControlPointCommand): RunMetricAfterControlPoint {
        val run = runRepository.getByRunId(command.runId)
        val event = run?.addControlPoint(command)
        run?.let { runRepository.saveRun(run) }

//        dodo cos tu architektonicznie jest nie tak
//        dodo jak ControlPoint jest nie z tej Route to co wtedy ? Exp z tlumaczeniem co poszlo nie tak ?
        event ?: throw Exception()
        return runReadModelFacade.updateOnAddedControlPointEvent(event)
    }

//    fun acceptRun(command: AcceptRunCommand) {
//    fun acceptRun(command: AcceptRunCommand) {
//        val run = runRepository.getByRunId(command.runId)
////        dodo or throw
//        val event = run?.accept(command)
//        run?.let { runRepository.saveRun(run) }
//        event?.let { runReadModelFacade.updateOnRunAcceptedEvent(it) }
//    }

//    fun changeRunCategory(command: ChangeRunCategoryCommand) {
//        val run = runRepository.getByRunId(command.runId)
//        val category = categoryFacade.getById(command.categoryId)
////        dodo NPE
//        val route = routeRepository.getByName(category!!.routeId)
//        val updatedCommand = command.copy(stations = route!!.stations)
//
//        val event = run?.changeCategory(updatedCommand)
//        run?.let { runRepository.saveRun(run) }
//        event?.let { runReadModelFacade.updateOnChangedRunCategoryEvent(it) }
//    }

//    fun startRun(command: StartRunCommand) {
//        val run = runRepository.getByRunId(command.runId)
////        dodo or throw
//        val event = run?.start(command)
//        run?.let { runRepository.saveRun(run) }
//        event?.let { runReadModelFacade.updateOnRunStartedEvent(it) }
//    }
//
//    fun addCheckpoint(command: AddCheckpointCommand) {
//        val run = runRepository.getByRunId(command.runId)
//        //        dodo or throw
//        val event = run?.addCheckpoint(command)
//        run?.let { runRepository.saveRun(run) }
//        event?.let { runReadModelFacade.updateOnAddedCheckpointEvent(it) }
//    }
//
//    fun finishRun(command: FinishRunCommand) {
//        val run = runRepository.getByRunId(command.runId)
//        //        dodo or throw
//        val event = run?.finish(command)
//        run?.let { runRepository.saveRun(run) }
//        event?.let { runReadModelFacade.updateOnRunFinishedEvent(it) }
//    }
}