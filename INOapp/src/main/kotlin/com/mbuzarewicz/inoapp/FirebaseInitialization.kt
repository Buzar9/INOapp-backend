package com.mbuzarewicz.inoapp

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.io.FileInputStream
import java.io.InputStream


@Service
class FirebaseInitialization {

    @Bean
    fun initialization(): Firestore {

        val serviceAccount: InputStream =
            FileInputStream("/Users/m.buzarewicz/IdeaProjects/INOapp-backend/INOapp/src/main/kotlin/com/mbuzarewicz/inoapp/serviceAccountKey.json")
        val credentials = GoogleCredentials.fromStream(serviceAccount)
        val options = FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build()
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }

        return FirestoreClient.getFirestore()
    }
}