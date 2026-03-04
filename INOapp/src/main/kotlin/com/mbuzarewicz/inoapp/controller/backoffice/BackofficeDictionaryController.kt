package com.mbuzarewicz.inoapp.controller.backoffice

import com.mbuzarewicz.inoapp.DictionaryFacade
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping(path = ["/backoffice/dictionaries"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class BackofficeDictionaryController(
    private val dictionaryFacade: DictionaryFacade
) {

    @GetMapping(value = ["/station"])
    @ResponseStatus(HttpStatus.OK)
    fun getStationDictionary(): ResponseEntity<*> {
        val stationDictionary = dictionaryFacade.getStationType()
        return ResponseEntity.status(200).body(stationDictionary)
    }

    @GetMapping(value = ["/status"])
    @ResponseStatus(HttpStatus.OK)
    fun getStatusDictionary(): ResponseEntity<*> {
        val stationDictionary = dictionaryFacade.getStatusType()
        return ResponseEntity.status(200).body(stationDictionary)
    }
}