package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Service

@Service
class RunFacade(
    private val runRepository: DefaultRunRepository,
    private val routeRepository: DefaultRouteRepository,
    private val constantStationRepository: DefaultConstantStationRepository,
    private val runReadModelFacade: RunReadModelFacade,
) {
    private val runPenaltyPolicyFactory = RunPenaltyPolicyFactory()

    fun initiateRun(command: InitiateRunCommand) {
//        dodo nałożyć na wszystko jeszcze warstwe organizatora, bo teraz wszystkie trasy sa wrzucane do jednego worka, a nie powinno tak byc
//        dodo troche kupa z exception
//        dodo w takim przypadku przy dodaniu nowej Penalty trzeba wszystkim trasom dodac nowe penalties
        val route = routeRepository.getByRouteId(command.routeName) ?: throw Exception()
//        val constantStations = constantStationRepository.getByRouteName(command.routeName)
//        val timePenaltyPolicy = TimePenaltyPolicy(route.penalties)
        val timePenaltyPolicy = runPenaltyPolicyFactory.resolve(command.routeName)
        val run = Run(
            runId = command.runId,
            routeName = command.routeName,
            competitionCategory = command.competitionCategory,
            constantStations = route.stations,
            timePenaltyPolicy = timePenaltyPolicy
        )
        val event = run.initiateRun(command)
        runRepository.saveRun(run)
        event?.let { runReadModelFacade.createIfNotExistOnRunInitializedEvent(event) }
    }

    fun startRun(command: StartRunCommand) {
        val run = runRepository.getByRunId(command.runId)
//        dodo or throw
        val event = run?.startRun(command)
        run?.let { runRepository.saveRun(run) }
        event?.let { runReadModelFacade.updateOnRunStartedEvent(event) }
    }

    fun addCheckpoint(command: AddCheckpointCommand) {
        val run = runRepository.getByRunId(command.runId)
        //        dodo or throw
        val event = run?.addCheckpoint(command)
        run?.let { runRepository.saveRun(run) }
        event?.let { runReadModelFacade.updateOnAddedCheckpointEvent(it) }
    }

    fun finishRun(command: FinishRunCommand) {
        val run = runRepository.getByRunId(command.runId)
        //        dodo or throw
        val event = run?.finishRun(command)
        run?.let { runRepository.saveRun(run) }
        event?.let { runReadModelFacade.updateOnRunFinishedEvent(it) }
    }

    fun addPenalty(command: AddPenaltyCommand) {
        val run = runRepository.getByRunId(command.runId)
        //        dodo or throw
//        run?.addPenalty(command)
    }
}