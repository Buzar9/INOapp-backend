package com.mbuzarewicz.inoapp.peristance.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.peristance.model.PersistableRun
import org.springframework.stereotype.Service

@Service
class FirestoreRunRepository(
    private val firestore: Firestore
) {

    private val collectionName = "run"

    fun getByRunId(id: String): PersistableRun? {
        val documentReference = firestore.collection(collectionName).document(id)
        val future = documentReference.get()
        val document = future.get()

        return if (document.exists()) {
            document.toObject(PersistableRun::class.java)
        } else {
            null
        }
    }

    fun getAll(): List<PersistableRun> {
        val documents = firestore.collection(collectionName).get().get().documents
        return documents.map { it.toObject(PersistableRun::class.java) }
    }

    fun save(persistableRun: PersistableRun) {
        val id = persistableRun.id
        val documentReference = if (id == null) {
            firestore.collection(collectionName).document()
        } else {
            firestore.collection(collectionName).document(id)
        }

        val result: ApiFuture<*> = documentReference.set(persistableRun, SetOptions.merge())
    }
}