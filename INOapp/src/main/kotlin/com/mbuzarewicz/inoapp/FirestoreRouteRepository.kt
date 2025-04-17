package com.mbuzarewicz.inoapp

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.SetOptions
import org.springframework.stereotype.Service

@Service
class FirestoreRouteRepository(
    private val firestore: Firestore
) {

    private val collectionName = "route"

    fun getByRouteName(routeName: String): PersistableRoute? {
        val query = firestore.collection(collectionName).whereEqualTo("routeName", routeName)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.firstNotNullOf { it.toObject(PersistableRoute::class.java) }
    }

    fun getAll(): List<PersistableRoute> {
        val documents = firestore.collection(collectionName).get().get().documents
        return documents.map { it.toObject(PersistableRoute::class.java) }
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