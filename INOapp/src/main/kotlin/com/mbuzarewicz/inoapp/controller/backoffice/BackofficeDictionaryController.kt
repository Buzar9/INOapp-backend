package com.mbuzarewicz.inoapp.controller.backoffice

import com.mbuzarewicz.inoapp.DictionaryFacade
import com.mbuzarewicz.inoapp.view.model.DictionaryModel
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RequestMapping(path = ["/backoffice/dictionaries"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class BackofficeDictionaryController(
    private val dictionaryFacade: DictionaryFacade
) {

    @GetMapping(value = ["/station"])
    @ResponseStatus(HttpStatus.OK)
    fun getStationDictionary(): ResponseEntity<List<DictionaryModel>> {
        val stationDictionary = dictionaryFacade.getStationType()
        return ResponseEntity.status(200).body(stationDictionary)
    }

    @GetMapping(value = ["/status"])
    @ResponseStatus(HttpStatus.OK)
    fun getStatusDictionary(): ResponseEntity<List<DictionaryModel>> {
        val stationDictionary = dictionaryFacade.getStatusType()
        return ResponseEntity.status(200).body(stationDictionary)
    }
}