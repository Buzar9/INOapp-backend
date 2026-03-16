package com.mbuzarewicz.inoapp.persistence.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.persistence.model.PersistableCategory
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class FirestoreCategoryRepository(
    private val firestore: Firestore
) {
    private val collectionName = "category"

    fun getActiveById(id: String): PersistableCategory? {
        val query = firestore.collection(collectionName).document(id)
        val future = query.get()
        val document = future.get(15, TimeUnit.SECONDS)

        return document.toObject(PersistableCategory::class.java)?.takeIf { it.active }
    }

    fun getAllActive(competitionId: String): List<PersistableCategory> {
        val query = firestore.collection(collectionName)
            .whereEqualTo("competitionId", competitionId)
            .whereEqualTo("active", true)
        val future = query.get()
        val snapshot = future.get(15, TimeUnit.SECONDS)

        return snapshot.documents.map { it.toObject(PersistableCategory::class.java) }
    }

    fun save(persistableCategory: PersistableCategory) {
        val documentReference = firestore.collection(collectionName).document(persistableCategory.id)

        val result: ApiFuture<*> = documentReference.set(persistableCategory, SetOptions.merge())
    }
}