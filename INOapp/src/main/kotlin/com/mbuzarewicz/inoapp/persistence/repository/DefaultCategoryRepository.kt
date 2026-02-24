package com.mbuzarewicz.inoapp.persistence.repository

import com.mbuzarewicz.inoapp.domain.model.Category
import com.mbuzarewicz.inoapp.persistence.mapper.PersistableCategoryMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultCategoryRepository(
    private val repository: FirestoreCategoryRepository
) {
    private val mapper = PersistableCategoryMapper()

    fun getActiveById(id: String): Category? {
        val persistableCategory = repository.getActiveById(id)
        return persistableCategory?.let { mapper.mapToDomainEntity(persistableCategory) }
    }

    fun getAllActive(competitionId: String): List<Category> {
        val persistableCategory = repository.getAllActive(competitionId)
        return persistableCategory.map { mapper.mapToDomainEntity(it) }
    }

    fun save(category: Category) {
        val persistableCategory = mapper.mapToPersistableEntity(category)
        repository.save(persistableCategory)
    }
}