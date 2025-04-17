package com.mbuzarewicz.inoapp

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Component
import java.io.File

@Component
class TranslationService {

    fun translate(category: String, key: String): String {
        val translations = loadTranslations(category)
        return translations[key] ?: "dodo brak tłumaczenia"
    }

    private fun loadTranslations(category: String): Map<String, String> {
        val file = File("/Users/m.buzarewicz/IdeaProjects/INOapp-backend/INOapp/src/main/resources/translations/${category}.json")
        val objectMapper = jacksonObjectMapper()
        return objectMapper.readValue(file)
    }
}