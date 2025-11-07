package com.mbuzarewicz.inoapp.peristance.repository

import com.mbuzarewicz.inoapp.domain.model.Category
import com.mbuzarewicz.inoapp.peristance.mapper.PersistableCategoryMapper
import org.springframework.stereotype.Repository

@Repository
class DefaultCategoryRepository(
    private val repository: FirestoreCategoryRepository
) {
    private val mapper = PersistableCategoryMapper()

    fun getById(id: String): Category? {
        val persistableCategory = repository.getById(id)
        return persistableCategory?.let { mapper.mapToDomainEntity(persistableCategory) }
    }

    fun getByNames(names: List<String>): List<Category>? {
        val persistableCategories = repository.getByCategoryName(names)
        return persistableCategories?.map { mapper.mapToDomainEntity(it) }
    }

    fun getAll(competitionId: String): List<Category> {
        val persistableCategory = repository.getAllByCompetitionId(competitionId)
        return persistableCategory.map { mapper.mapToDomainEntity(it) }
    }

    fun save(category: Category) {
        val persistableCategory = mapper.mapToPersistableEntity(category)
        repository.save(persistableCategory)
    }

    fun delete(categoryId: String) {
        repository.delete(categoryId)
    }
}