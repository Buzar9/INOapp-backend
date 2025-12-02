package com.mbuzarewicz.inoapp.persistence.repository

import com.google.cloud.firestore.Firestore
import com.mbuzarewicz.inoapp.persistence.model.PersistableRunTrack
import com.mbuzarewicz.inoapp.persistence.model.PersistableRunTrackPoint
import org.springframework.stereotype.Component

@Component
class FirestoreRunTrackRepository(
    private val firestore: Firestore
) {
    private val collectionName = "run-track"

    fun save(track: PersistableRunTrack) {
        firestore
            .collection(collectionName)
            .document(track.id)
            .set(track)
            .get()
    }

    fun appendPoints(id: String, newPoints: List<PersistableRunTrackPoint>) {
        val docRef = firestore
            .collection(collectionName)
            .document(id)

        firestore.runTransaction { transaction ->
            val snapshot = transaction.get(docRef).get()
            val existingTrack = snapshot.toObject(PersistableRunTrack::class.java)

            val updatedPoints = if (existingTrack != null) {
                existingTrack.points + newPoints
            } else {
                newPoints
            }

            val updatedTrack = existingTrack?.copy(points = updatedPoints)
                ?: PersistableRunTrack(id = id, points = updatedPoints)

            transaction.set(docRef, updatedTrack)
            null
        }.get()
    }

    fun findByRunId(runId: String): PersistableRunTrack? {
        return firestore
            .collection(collectionName)
            .document(runId)
            .get()
            .get()
            .toObject(PersistableRunTrack::class.java)
    }
}

