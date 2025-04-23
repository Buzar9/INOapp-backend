package com.mbuzarewicz.inoapp

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component
import java.io.File

@Component
class TranslationService(
    private val resourceLoader: ResourceLoader
) {
    private val objectMapper = jacksonObjectMapper()

    fun translate(category: String, key: String): String {
        val translations = loadTranslations(category)
        return translations[key] ?: "dodo brak tłumaczenia"
    }

    private fun loadTranslations(category: String): Map<String, String> {
        val resource = resourceLoader.getResource("classpath:translations/${category}.json")
        return  resource.inputStream.use { inputStream ->
            objectMapper.readValue(inputStream, Map::class.java) as Map<String, String>
        }
    }
}