package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CategoryFacade
import com.mbuzarewicz.inoapp.command.CreateCategoryCommand
import com.mbuzarewicz.inoapp.command.DeleteCategoryCommand
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/backoffice/categories"], produces = [MediaType.APPLICATION_JSON_VALUE])
class BackofficeCategoryController(
    private val categoryFacade: CategoryFacade
) {

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    fun create(
        @RequestHeader("X-Competition-Id") competitionId: String,
        @RequestBody request: CreateCategoryRequest
    ) {
        categoryFacade.create(request.toCommand(competitionId))
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CreateCategoryRequest(
        val name: String,
        val routeId: String,
    )

    private fun CreateCategoryRequest.toCommand(competitionId: String) =
        CreateCategoryCommand(name = name, competitionId = competitionId, routeId = routeId)

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    fun deactivate(@RequestBody request: DeleteCategoryRequest) {
        categoryFacade.deactivate(request.toCommand())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteCategoryRequest(
        val categoryId: String
    )

    private fun DeleteCategoryRequest.toCommand() =
        DeleteCategoryCommand(categoryId)

    @GetMapping
    fun get(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<*> {
        val results = categoryFacade.getAllActive(competitionId)
        return ResponseEntity.status(200).body(results)
    }
}
