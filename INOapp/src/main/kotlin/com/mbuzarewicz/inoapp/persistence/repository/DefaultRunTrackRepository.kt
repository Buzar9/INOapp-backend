package com.mbuzarewicz.inoapp.persistence.repository

import com.mbuzarewicz.inoapp.domain.model.RunTrack
import com.mbuzarewicz.inoapp.domain.model.RunTrackPoint
import com.mbuzarewicz.inoapp.persistence.mapper.PersistableRunTrackMapper
import com.mbuzarewicz.inoapp.persistence.mapper.PersistableRunTrackPointMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultRunTrackRepository(
    private val repository: FirestoreRunTrackRepository
) {
    private val trackMapper = PersistableRunTrackMapper()
    private val pointMapper = PersistableRunTrackPointMapper()

    fun appendPoints(runId: String, points: List<RunTrackPoint>) {
        val persistablePoints = points.map { pointMapper.mapToPersistableEntity(it)}
        repository.appendPoints(runId, persistablePoints)
    }

    fun findByRunId(runId: String): RunTrack? {
        val persistableTrack = repository.findByRunId(runId) ?: return null
        return trackMapper.mapToDomainEntity(persistableTrack)
    }
}

