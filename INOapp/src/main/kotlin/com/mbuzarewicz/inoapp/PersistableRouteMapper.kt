package com.mbuzarewicz.inoapp

class PersistableRouteMapper {
    private val constantStationMapper = PersistableConstantStationMapper()
    private val timePenaltyRuleMapper = PersistableTimePenaltyRuleMapper()

    fun mapToPersistableEntity(domain: Route): PersistableRoute {
        return with(domain) {
            PersistableRoute(
                id = databaseId,
                routeName = routeName,
                stations = stations.map { constantStationMapper.mapToPersistableEntity(it) },
//                penalties = penalties.mapToPersistable()
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRoute): Route {
        return with(persistable) {
            Route(
                databaseId = id,
                routeName = routeName,
                stations = stations.map { constantStationMapper.mapToDomainEntity(it) },
//                penalties = penalties.mapToDomain()
            )
        }
    }

    private fun Map<PenaltyCause, TimePenaltyRule>.mapToPersistable(): Map<String, PersistableTimePenaltyRule> {
        return this.map { (key, value) ->
            key.toString() to timePenaltyRuleMapper.mapToPersistableEntity(value)
        }.toMap()
    }

    private fun Map<String, PersistableTimePenaltyRule>.mapToDomain(): Map<PenaltyCause, TimePenaltyRule> {
        return this.map { (key, value) ->
            PenaltyCause.valueOf(key) to timePenaltyRuleMapper.mapToDomainEntity(value)
        }.toMap()
    }
}