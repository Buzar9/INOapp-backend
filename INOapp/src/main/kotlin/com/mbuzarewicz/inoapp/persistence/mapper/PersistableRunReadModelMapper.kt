package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.RunReadModel
import com.mbuzarewicz.inoapp.RunStatus
import com.mbuzarewicz.inoapp.persistence.model.PersistableRunReadModel

class PersistableRunReadModelMapper {
    private val controlPointMapper = PersistableControlPointMapper()

    fun mapToPersistableEntity(domain: RunReadModel): PersistableRunReadModel {
        return with(domain) {
            PersistableRunReadModel(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
                controlPoints = controlPoints.map { controlPointMapper.mapToPersistableEntity(it) },
                participantNickname = participantName,
                participantUnit = participantUnit,
                status = status.toString(),
                startTime = startTime,
                finishTime = finishTime,
                mainTime = mainTime
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRunReadModel): RunReadModel {
        return with(persistable) {
            RunReadModel(
                id = id,
                categoryId = categoryId,
                competitionId = competitionId,
                controlPoints = controlPoints.map { controlPointMapper.mapToDomainEntity(it) },
                participantName = participantNickname,
                participantUnit = participantUnit,
                status = RunStatus.valueOf(status),
                startTime = startTime,
                finishTime = finishTime,
                mainTime = mainTime
            )
        }
    }
}