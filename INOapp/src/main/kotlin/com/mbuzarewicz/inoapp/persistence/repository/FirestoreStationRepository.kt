package com.mbuzarewicz.inoapp.persistence.repository

import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.persistence.model.PersistablePatternStation
import org.springframework.stereotype.Service

@Service
class FirestoreStationRepository(
    private val firestore: Firestore
) {

    private val collectionName = "station"

    fun findAll(): List<PersistablePatternStation> {
        val documents = firestore.collection(collectionName).get().get().documents
        return documents.map { it.toObject(PersistablePatternStation::class.java) }
    }

    fun save(persistable: PersistablePatternStation) {
        val id = persistable.id
        val documentReference = firestore.collection(collectionName).document(id)
        documentReference.set(persistable, SetOptions.merge())
    }
}