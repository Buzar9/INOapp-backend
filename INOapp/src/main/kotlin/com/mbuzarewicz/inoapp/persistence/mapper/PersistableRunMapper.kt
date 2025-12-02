package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.domain.model.Run
import com.mbuzarewicz.inoapp.persistence.model.PersistableRun

class PersistableRunMapper {
    private val constantStationMapper = PersistableStationMapper()
    private val checkpointMapper = PersistableControlPointMapper()

    fun mapToPersistableEntity(domain: Run): PersistableRun {
        return with(domain) {
            PersistableRun(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
                stations = stations.map { constantStationMapper.mapToPersistableEntity(it) },
                controlPoints = controlPoints.map { checkpointMapper.mapToPersistableEntity(it) },
                startTime = startTime,
                finishTime = finishTime,
                status = status.toString()
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRun): Run {
        return with(persistable) {
            Run.recreate(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
                stations = stations.map { constantStationMapper.mapToDomainEntity(it) },
                controlPoints = controlPoints.map { checkpointMapper.mapToDomainEntity(it) }.toMutableList(),
                startTime = startTime,
                finishTime = finishTime,
                status = RunStatus.valueOf(status)
            )
        }
    }
}