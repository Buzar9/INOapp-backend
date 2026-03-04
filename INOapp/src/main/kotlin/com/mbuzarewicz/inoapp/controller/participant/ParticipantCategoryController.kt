package com.mbuzarewicz.inoapp.controller.participant

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CategoryFacade
import com.mbuzarewicz.inoapp.query.GetStationsByCategoryIdQuery
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/categories"], produces = [MediaType.APPLICATION_JSON_VALUE])
@CrossOrigin(origins = ["http://localhost:4200"])
class ParticipantCategoryController(
    private val categoryFacade: CategoryFacade
) {

    @PostMapping("/stations")
    fun getStations(@RequestBody request: GetStationsForCategoryRequest): ResponseEntity<*> {
        val result = categoryFacade.getStationsGeoViewByCategoryId(request.toQuery())
        return ResponseEntity.status(200).body(result)
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GetStationsForCategoryRequest(
        val categoryId: String
    )

    private fun GetStationsForCategoryRequest.toQuery() = GetStationsByCategoryIdQuery(categoryId)
}