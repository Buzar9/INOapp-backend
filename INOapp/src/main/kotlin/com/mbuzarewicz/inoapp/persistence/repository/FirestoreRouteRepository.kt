package com.mbuzarewicz.inoapp.persistence.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.persistence.model.PersistableRoute
import org.springframework.stereotype.Service

@Service
class FirestoreRouteRepository(
    private val firestore: Firestore
) {
    private val collectionName = "route"

    fun findById(id: String): PersistableRoute? {
        val query = firestore.collection(collectionName).document(id)
        val future = query.get()
        val snapshot = future.get()

        return if (snapshot.exists()) {
            snapshot.toObject(PersistableRoute::class.java)
        } else {
            null
        }
    }

    fun getByRouteName(routeName: String): PersistableRoute? {
        val query = firestore.collection(collectionName).whereEqualTo("routeName", routeName)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.firstNotNullOf { it.toObject(PersistableRoute::class.java) }
    }

    fun getAll(competitionId: String): List<PersistableRoute> {
        val query = firestore.collection(collectionName).whereEqualTo("competitionId", competitionId)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.map { it.toObject(PersistableRoute::class.java) }
    }

    fun getAllActive(competitionId: String): List<PersistableRoute> {
        val query = firestore.collection(collectionName)
            .whereEqualTo("competitionId", competitionId)
            .whereEqualTo("active", true)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.map { it.toObject(PersistableRoute::class.java) }
    }

    fun save(persistableRoute: PersistableRoute) {
        val id = persistableRoute.id
        val documentReference = if (id == null) {
            firestore.collection(collectionName).document()
        } else {
            firestore.collection(collectionName).document(id)
        }

        val result: ApiFuture<*> = documentReference.set(persistableRoute, SetOptions.merge())
    }

    fun delete(id: String) {
        val documentReference = firestore.collection(collectionName).document(id)
        documentReference.delete()
    }
}