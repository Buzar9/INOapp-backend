package com.mbuzarewicz.inoapp.peristance.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.peristance.model.PersistableCompetitionUnit
import org.springframework.stereotype.Service

@Service
class FirestoreCompetitionUnitRepository(
    private val firestore: Firestore
) {
    private val collectionName = "competition-unit"

    fun save(persistableCompetitionUnit: PersistableCompetitionUnit) {
        val id = persistableCompetitionUnit.id
        val documentReference = if (id == null) {
            firestore.collection(collectionName).document()
        } else {
            firestore.collection(collectionName).document(id)
        }

        val result: ApiFuture<*> = documentReference.set(persistableCompetitionUnit, SetOptions.merge())
    }

    fun getAll(competitionId: String): List<PersistableCompetitionUnit> {
        val query = firestore.collection(collectionName).whereEqualTo("competitionId", competitionId)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.map { it.toObject(PersistableCompetitionUnit::class.java) }
    }

    fun findById(id: String): PersistableCompetitionUnit? {
        val query = firestore.collection(collectionName).document(id)
        val future = query.get()
        val snapshot = future.get()

        return if (snapshot.exists()) {
            snapshot.toObject(PersistableCompetitionUnit::class.java)
        } else {
            null
        }
    }

    fun delete(id: String) {
        val documentReference = firestore.collection(collectionName).document(id)
        documentReference.delete()
    }
}