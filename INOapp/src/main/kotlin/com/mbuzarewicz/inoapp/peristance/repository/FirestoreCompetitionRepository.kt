package com.mbuzarewicz.inoapp.peristance.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.peristance.model.PersistableCompetition
import org.springframework.stereotype.Service

@Service
class FirestoreCompetitionRepository(
    private val firestore: Firestore
) {
    private val collectionName = "competition"

    fun save(competition: PersistableCompetition) {
        val query = firestore.collection(collectionName).whereEqualTo("name", competition.name)
        val future = query.get()
        val snapshot = future.get()

//        NotUniqueException
        if (!snapshot.isEmpty) {
            throw Exception("Konkurencja o nazwie '${competition.name}' już istnieje.")
        }

        val documentReference = firestore.collection(collectionName).document()
        val result: ApiFuture<*> = documentReference.set(competition, SetOptions.merge())
    }

    fun findByName(name: String): PersistableCompetition {
        val query = firestore.collection(collectionName).whereEqualTo("name", name)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.firstNotNullOf { it.toObject(PersistableCompetition::class.java) }
    }

    fun findAll(): List<PersistableCompetition> {
        val documents = firestore.collection(collectionName).get().get().documents
        return documents.map { it.toObject(PersistableCompetition::class.java) }
    }
}