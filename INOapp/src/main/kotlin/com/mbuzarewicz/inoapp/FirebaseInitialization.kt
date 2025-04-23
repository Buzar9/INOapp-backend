package com.mbuzarewicz.inoapp

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Service
import java.io.ByteArrayInputStream
import java.io.FileInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets


@Service
class FirebaseInitialization(
    @Value("\${firestore.credentials}")
    private val firebaseCredentials: String,
) {

    @Bean
    fun initialization(): Firestore {

        val credentialsStream = ByteArrayInputStream(firebaseCredentials.toByteArray(StandardCharsets.UTF_8))
        val credentials = GoogleCredentials.fromStream(credentialsStream)
        val options = FirebaseOptions.Builder()
            .setCredentials(credentials)
            .build()
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }

        return FirestoreClient.getFirestore()
    }
}