package com.mbuzarewicz.inoapp.peristance.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.peristance.model.PersistableBackgroundMap
import org.springframework.stereotype.Service

@Service
class FirestoreBackgroundMapRepository(
    private val firestore: Firestore
) {
    private val collectionName = "background-map"

    fun save(backgroundMap: PersistableBackgroundMap) {
        val documentReference = firestore.collection(collectionName).document(backgroundMap.id)
        val result: ApiFuture<*> = documentReference.set(backgroundMap, SetOptions.merge())
    }

    fun getAll(): List<PersistableBackgroundMap> {
        val query = firestore.collection(collectionName).get()
        val snapshot = query.get()
        return snapshot.documents.mapNotNull { it.toObject(PersistableBackgroundMap::class.java) }
    }

    fun getByIdIn(backgroundIds: List<String>): List<PersistableBackgroundMap> {
        val query = firestore.collection(collectionName)
            .whereIn("__name__", backgroundIds.map { firestore.collection(collectionName).document(it).path })
            .get()
        val snapshot = query.get()
        return snapshot.documents.mapNotNull { it.toObject(PersistableBackgroundMap::class.java) }
    }

    fun getById(backgroundMapId: String): PersistableBackgroundMap? {
        val documentReference = firestore.collection(collectionName).document(backgroundMapId)
        val snapshot = documentReference.get().get()
        return snapshot.toObject(PersistableBackgroundMap::class.java)
    }
}