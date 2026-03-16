package com.mbuzarewicz.inoapp.persistence.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.persistence.model.PersistableCompetition
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class FirestoreCompetitionRepository(
    private val firestore: Firestore
) {
    private val collectionName = "competition"

    fun save(competition: PersistableCompetition) {
        val query = firestore.collection(collectionName).whereEqualTo("name", competition.name)
        val future = query.get()
        val snapshot = future.get(15, TimeUnit.SECONDS)

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
        val snapshot = future.get(15, TimeUnit.SECONDS)

        return snapshot.documents.firstNotNullOf { it.toObject(PersistableCompetition::class.java) }
    }

    fun findAll(): List<PersistableCompetition> {
        val documents = firestore.collection(collectionName).get().get(15, TimeUnit.SECONDS).documents
        return documents.map { it.toObject(PersistableCompetition::class.java) }
    }
}