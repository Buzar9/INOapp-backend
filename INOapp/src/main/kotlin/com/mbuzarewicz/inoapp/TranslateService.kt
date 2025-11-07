package com.mbuzarewicz.inoapp

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.io.IOException

object TranslateService {
    private val objectMapper = jacksonObjectMapper()

    fun translate(category: String, key: String): String {
        return try {
            val translations = loadTranslations(category)
            translations[key] ?: key
        } catch (e: IOException) {
            println("Warning: Translations for category '$category' could not be loaded. Returning key '$key'.")
            key
        }
    }

    private fun loadTranslations(category: String): Map<String, String> {
        val resourcePath = "translations/${category}.json"
        val inputStream = javaClass.classLoader.getResourceAsStream(resourcePath)
            ?: throw IOException("Resource '$resourcePath' not found.")

        val typeReference = object : TypeReference<Map<String, String>>() {}
        return inputStream.use {
            objectMapper.readValue(it, typeReference)
        }
    }
}