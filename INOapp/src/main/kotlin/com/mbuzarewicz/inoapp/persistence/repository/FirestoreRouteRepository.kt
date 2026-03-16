package com.mbuzarewicz.inoapp.persistence.repository

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.FieldPath
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import com.mbuzarewicz.inoapp.persistence.model.PersistableRoute
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class FirestoreRouteRepository(
    private val firestore: Firestore
) {
    private val collectionName = "route"

    fun findActiveById(id: String): PersistableRoute? {
        val query = firestore.collection(collectionName).document(id)
        val future = query.get()
        val document = future.get(15, TimeUnit.SECONDS)

        return document.toObject(PersistableRoute::class.java)?.takeIf { it.active }
    }

    fun getAllActive(competitionId: String): List<PersistableRoute> {
        val query = firestore.collection(collectionName)
            .whereEqualTo("competitionId", competitionId)
            .whereEqualTo("active", true)
        val future = query.get()
        val snapshot = future.get(15, TimeUnit.SECONDS)

        return snapshot.documents.map { it.toObject(PersistableRoute::class.java) }
    }

    fun getActiveByIds(routeIds: List<String>): List<PersistableRoute> {
        if (routeIds.isEmpty()) return emptyList()
        val documentRefs = routeIds.map { firestore.collection(collectionName).document(it) }
        val query = firestore.collection(collectionName)
            .whereIn(FieldPath.documentId(), documentRefs)
            .whereEqualTo("active", true)
            .get()
        val snapshot = query.get(15, TimeUnit.SECONDS)
        return snapshot.documents.mapNotNull { it.toObject(PersistableRoute::class.java) }
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
}