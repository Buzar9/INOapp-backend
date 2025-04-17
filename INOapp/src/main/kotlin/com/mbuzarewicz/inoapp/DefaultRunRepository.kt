package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Repository

@Repository
class DefaultRunRepository(
    private val repository: FirestoreRunRepository
) {
    private val mapper = PersistableRunMapper()

    fun getByRunId(runId: String): Run? {
        val persistableRun = repository.getByRunId(runId)
        return persistableRun?.let { mapper.mapToDomainEntity(persistableRun) }
    }

    fun getAll(): List<Run> {
        val persistableRuns = repository.getAll()
        return persistableRuns.map { mapper.mapToDomainEntity(it) }
    }

    fun saveRun(run: Run) {
        val persistableRun = mapper.mapToPersistableEntity(run)
        repository.save(persistableRun)
    }
}