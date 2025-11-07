package com.mbuzarewicz.inoapp.peristance.repository

import com.mbuzarewicz.inoapp.RunReadModel
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableRunReadModelMapper
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

    fun getFiltered(competitionUnit: List<String>, categoryId: List<String>, status: List<String>): List<RunReadModel> {
        val persistableRunReadModels = repository.getFiltered(competitionUnit, categoryId, status)
        return persistableRunReadModels.map { mapper.mapToDomainEntity(it) }
    }

    fun save(runReadModel: RunReadModel) {
        val persistableRun = mapper.mapToPersistableEntity(runReadModel)
        repository.save(persistableRun)
    }
}