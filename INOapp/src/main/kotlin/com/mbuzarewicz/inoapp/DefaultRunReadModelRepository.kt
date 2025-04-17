package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Repository

@Repository
class DefaultRunReadModelRepository(
    private val repository: FirestoreRunReadModelRepository
) {
    private val mapper = PersistableRunReadModelMapper()

    fun getByRunId(runId: String): RunReadModel? {
        val persistableRun = repository.getByRunId(runId)
        return persistableRun?.let { mapper.mapToDomainEntity(persistableRun) }
    }

    fun getAll(): List<RunReadModel> {
        val persistableRunReadModels = repository.getAll()
        return persistableRunReadModels.map { mapper.mapToDomainEntity(it) }
    }

    fun save(runReadModel: RunReadModel) {
        val persistableRun = mapper.mapToPersistableEntity(runReadModel)
        repository.save(persistableRun)
    }

    fun saveIfNotExist(runReadModel: RunReadModel) {
        val persistableRun = mapper.mapToPersistableEntity(runReadModel)
//        dodo do sprawdzenia czy tak to zadziala
        if (persistableRun.id != null) repository.save(persistableRun)
    }
}