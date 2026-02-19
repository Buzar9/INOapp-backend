package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.domain.model.Run
import com.mbuzarewicz.inoapp.persistence.model.PersistableRun

class PersistableRunMapper {
    private val checkpointMapper = PersistableControlPointMapper()

    fun mapToPersistableEntity(domain: Run): PersistableRun {
        return with(domain) {
            PersistableRun(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
//                !dodo sprawdzic czy mozna usunac
                stations = mutableListOf(),
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
                controlPoints = controlPoints.map { checkpointMapper.mapToDomainEntity(it) }.toMutableList(),
                startTime = startTime,
                finishTime = finishTime,
                status = RunStatus.valueOf(status)
            )
        }
    }
}