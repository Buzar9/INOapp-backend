package com.mbuzarewicz.inoapp

class PersistableRunMapper {
    private val constantStationMapper = PersistableConstantStationMapper()
    private val validationResultMapper = PersistableValidationResultMapper()
    private val checkpointMapper = PersistableCheckpointMapper()
    private val penaltyMapper = PersistablePenaltyMapper()
    private val timePenaltyPolicyFactory = RunPenaltyPolicyFactory()

    fun mapToPersistableEntity(domain: Run): PersistableRun {
        return with(domain) {
            PersistableRun(
                id = runId,
                routeName = routeName,
                competitionCategory = competitionCategory,
                constantStations = constantStations.map { constantStationMapper.mapToPersistableEntity(it) },
                visitedCheckpoints = visitedCheckpoints.map { checkpointMapper.mapToPersistableEntity(it) },
                stationsValidationResults = stationsValidationResults.mapToPersistable(),
                penalties = penalties.map { penaltyMapper.mapToPersistableEntity(it) },
                startTime = startTime,
                finishTime = finishTime,
                status = status
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRun): Run {
        return with(persistable) {
            val timePenaltyPolicy = timePenaltyPolicyFactory.resolve(routeName)
            Run(
                runId = id,
                routeName = routeName,
                competitionCategory = competitionCategory,
                visitedCheckpoints = visitedCheckpoints.map { checkpointMapper.mapToDomainEntity(it) }.toMutableList(),
                stationsValidationResults = stationsValidationResults.mapToDomain(),
                constantStations = constantStations.map { constantStationMapper.mapToDomainEntity(it) },
                timePenaltyPolicy = timePenaltyPolicy,
                penalties = penalties.map { penaltyMapper.mapToDomainEntity(it) }.toMutableList(),
                startTime = startTime,
                finishTime = finishTime,
                status = status
            )
        }
    }

    private fun MutableMap<CheckpointId, List<ValidationResult>>.mapToPersistable(): Map<String, List<PersistableValidationResult>> {
        return this.mapKeys { (checkpointId, _) -> checkpointId.mapToStringKey() }
            .mapValues { (_, value) -> value.map { validationResultMapper.mapToPersistableEntity(it) } }
            .toMap()
    }

    private fun Map<String, List<PersistableValidationResult>>.mapToDomain(): MutableMap<CheckpointId, List<ValidationResult>> {
        return this.mapKeys { (key, _) -> key.splitToIdAndRouteName() }
            .mapValues { (_, value) -> value.map { validationResultMapper.mapToDomainEntity(it) } }
            .toMutableMap()
    }

    private fun CheckpointId.mapToStringKey() = "${this.routeName}-${this.id}"

    private fun String.splitToIdAndRouteName(): CheckpointId {
        val index = this.indexOf('-')
        require(index != -1) { "Brak myślnika w stringu!" }
        val routeName = this.substring(0, index)
        val id = this.substring(index + 1)
        return CheckpointId(id, routeName)
    }
}