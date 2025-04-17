package com.mbuzarewicz.inoapp

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import org.springframework.stereotype.Service

@Service
class FirestoreRunReadModelRepository(
    private val firestore: Firestore
) {

    private val collectionName = "run-read-model"

    fun getByRunId(id: String): PersistableRunReadModel? {
        val documentReference = firestore.collection(collectionName).document(id)
        val future = documentReference.get()
        val document = future.get()

        return if (document.exists()) {
            document.toObject(PersistableRunReadModel::class.java)
        } else {
            null
        }
    }

    fun getAll(): List<PersistableRunReadModel> {
        val documents = firestore.collection(collectionName).get().get().documents
        return documents.map { it.toObject(PersistableRunReadModel::class.java) }
    }

    fun save(persistableRunReadModel: PersistableRunReadModel) {
        val id = persistableRunReadModel.id
        val documentReference = if (id == null) {
            firestore.collection(collectionName).document()
        } else {
            firestore.collection(collectionName).document(id)
        }

        val result: ApiFuture<*> = documentReference.set(persistableRunReadModel, SetOptions.merge())
    }
}