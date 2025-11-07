package com.mbuzarewicz.inoapp.controller.participant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.BackgroundMapFacade
import com.mbuzarewicz.inoapp.CategoryFacade
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(path = ["/background_maps"], produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["http://localhost:4200"])
class ParticipantBackgroundMapController(
    private val backgroundMapFacade: BackgroundMapFacade,
    private val categoryFacade: CategoryFacade,
) {

    @PostMapping
    fun getBackgroundMap(
        @RequestBody request: GetBackgroundMapRequest
    ): ResponseEntity<BackgroundMapView> {
//        dodo troche kupa, bo nie nie wiadomo gdzie to ma byc w backgroundMap czy Category?
        val category = categoryFacade.getById(request.categoryId)
        val backgroundMap = backgroundMapFacade.getViewById(category!!.backgroundMapId)
        return ResponseEntity.status(200).body(backgroundMap)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GetBackgroundMapRequest(
        val competitionId: String,
        val categoryId: String,
    )
}