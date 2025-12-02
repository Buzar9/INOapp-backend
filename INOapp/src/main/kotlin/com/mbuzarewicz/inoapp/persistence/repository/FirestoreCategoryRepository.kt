package com.mbuzarewicz.inoapp.persistence.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.persistence.model.PersistableCategory
import org.springframework.stereotype.Service

@Service
class FirestoreCategoryRepository(
    private val firestore: Firestore
) {
    private val collectionName = "category"

    fun getById(id: String): PersistableCategory? {
        val documentReference = firestore.collection(collectionName).document(id)
        val future = documentReference.get()
        val document = future.get()

        return document.toObject(PersistableCategory::class.java)
    }

    fun getByCategoryName(names: List<String>): List<PersistableCategory>? {
        val query = firestore.collection(collectionName).whereIn("name", names)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.map { it.toObject(PersistableCategory::class.java) }
    }

    fun getAllByCompetitionId(competitionId: String): List<PersistableCategory> {
        val query = firestore.collection(collectionName).whereEqualTo("competitionId", competitionId)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.map { it.toObject(PersistableCategory::class.java) }
    }

    fun save(persistableCategory: PersistableCategory) {
        val documentReference = firestore.collection(collectionName).document(persistableCategory.id)

        val result: ApiFuture<*> = documentReference.set(persistableCategory, SetOptions.merge())
    }

    fun delete(id: String) {
        val documentReference = firestore.collection(collectionName).document(id)
        documentReference.delete()
    }
}