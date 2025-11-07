package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.CategoryFacade
import com.mbuzarewicz.inoapp.command.CreateCategoryCommand
import com.mbuzarewicz.inoapp.command.DeleteCategoryCommand
import com.mbuzarewicz.inoapp.view.model.CategoryView
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
    fun create(@RequestBody request: CreateCategoryRequest) {
        categoryFacade.create(request.toCommand())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class CreateCategoryRequest(
        val name: String,
        val routeId: String,
//        dodo mock
        val maxTime: Long = 4,
//        dodo mock
        val competitionId: String = "Competition123",
        val backgroundMapId: String,
    )

    private fun CreateCategoryRequest.toCommand() =
//        dodo mock backgroundMapName
        CreateCategoryCommand(name = name, competitionId = competitionId, routeId = routeId, maxTime = maxTime, backgroundMapId = backgroundMapId)

    @PostMapping("/delete")
    @ResponseStatus(HttpStatus.OK)
    fun delete(@RequestBody request: DeleteCategoryRequest) {
        categoryFacade.delete(request.toCommand())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteCategoryRequest(
        val categoryId: String
    )

    private fun DeleteCategoryRequest.toCommand() =
        DeleteCategoryCommand(categoryId)

    @GetMapping
    fun get(): ResponseEntity<List<CategoryView>> {
        val results = categoryFacade.getAll()
        return ResponseEntity.status(200).body(results)
    }
}