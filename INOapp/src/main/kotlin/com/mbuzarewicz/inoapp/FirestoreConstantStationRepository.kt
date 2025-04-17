package com.mbuzarewicz.inoapp

import com.google.cloud.firestore.Firestore
import org.springframework.stereotype.Service

@Service
class FirestoreConstantStationRepository(
    private val firestore: Firestore
) {

    private val collectionName = "constant-station"

    fun getByRouteName(routeName: String): List<PersistableConstantStation> {
        val query = firestore.collection(collectionName).whereEqualTo("routeName", routeName)
        val future = query.get()
        val snapshot = future.get()

        return snapshot.documents.mapNotNull { it.toObject(PersistableConstantStation::class.java) }
    }
}