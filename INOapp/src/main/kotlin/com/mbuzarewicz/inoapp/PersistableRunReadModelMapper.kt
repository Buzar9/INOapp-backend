package com.mbuzarewicz.inoapp

class PersistableRunReadModelMapper {
    private val validationResultMapper = PersistableValidationResultMapper()
    private val checkpointMapper = PersistableCheckpointMapper()
    private val penaltyMapper = PersistablePenaltyMapper()

    fun mapToPersistableEntity(domain: RunReadModel): PersistableRunReadModel {
        return with(domain) {
            PersistableRunReadModel(
                id = runId,
                nickname = nickname,
                team = team,
                routeName = routeName,
                competitionCategory = competitionCategory,
                status = status,
                startTime = startTime,
                finishTime = finishTime,
                mainTime = mainTime,
                totalTime = totalTime,
                visitedCheckpointsNumber = visitedCheckpointsNumber,
                visitedCheckpoints = visitedCheckpoints.map { checkpointMapper.mapToPersistableEntity(it) },
                stationsValidationResults = stationsValidationResults.mapToPersistableMap(),
                penalties = penalties.map { penaltyMapper.mapToPersistableEntity(it) },
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRunReadModel): RunReadModel {
        return with(persistable) {
            RunReadModel(
                runId = id,
                nickname = nickname,
                team = team,
                routeName = routeName,
                competitionCategory = competitionCategory,
                status = status!!,
                startTime = startTime,
                finishTime = finishTime,
                mainTime = mainTime,
                totalTime = totalTime,
                visitedCheckpointsNumber = visitedCheckpointsNumber,
                visitedCheckpoints = visitedCheckpoints.map { checkpointMapper.mapToDomainEntity(it) }.toMutableList(),
                stationsValidationResults = stationsValidationResults.mapToDomainMap(),
                penalties = penalties.map { penaltyMapper.mapToDomainEntity(it) }.toMutableList()
            )
        }
    }

    private fun MutableMap<CheckpointId, List<ValidationResult>>.mapToPersistableMap(): Map<String, List<PersistableValidationResult>> {
        return this.mapKeys { (checkpointId, _) -> checkpointId.mapToStringKey() }
            .mapValues { (_, value) -> value.map { validationResultMapper.mapToPersistableEntity(it) } }
            .toMap()
    }

    private fun Map<String, List<PersistableValidationResult>>.mapToDomainMap(): MutableMap<CheckpointId, List<ValidationResult>> {
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