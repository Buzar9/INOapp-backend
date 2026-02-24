package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.CreateCategoryCommand
import com.mbuzarewicz.inoapp.command.DeleteCategoryCommand
import com.mbuzarewicz.inoapp.domain.model.Category
import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.persistence.repository.DefaultCategoryRepository
import com.mbuzarewicz.inoapp.query.GetStationsByCategoryIdQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewCategoryMapper
import com.mbuzarewicz.inoapp.view.mapper.ViewGeoMapper
import com.mbuzarewicz.inoapp.view.model.CategoryView
import com.mbuzarewicz.inoapp.view.model.GeoView
import org.springframework.stereotype.Service
import java.util.*

@Service
class CategoryFacade(
    private val categoryRepository: DefaultCategoryRepository,
    private val routeFacade: RouteFacade,
    private val backgroundMapFacade: BackgroundMapFacade
) {
    private val geoViewMapper = ViewGeoMapper()
    private val categoryViewMapper = ViewCategoryMapper()

    //    dodo założyć unikalne name dla danej competition
    fun create(command: CreateCategoryCommand) {
        val route = routeFacade.getRoute(command.routeId) ?: throw IllegalArgumentException("Route with id ${command.routeId} not found")

        val category = Category(
            id = UUID.randomUUID().toString(),
            name = command.name,
            competitionId = command.competitionId,
            routeId = command.routeId,
//            dodo mock
            maxTime = 4,
            backgroundMapId = route.backgroundMapId,
            isActive = true
        )
        categoryRepository.save(category)
    }

    fun getStationsGeoViewByCategoryId(query: GetStationsByCategoryIdQuery): List<GeoView> {
        val stations = getStationsByCategoryId(query.categoryId)
        return stations.map { geoViewMapper.mapToView(it) }
    }

    fun getStationsByCategoryId(categoryId: String): List<Station> {
        val category = categoryRepository.getActiveById(categoryId) ?: throw Exception()
        val route = routeFacade.getRoute(category.routeId) ?: throw Exception()
        return route.stations
    }

    fun getAllActive(): List<CategoryView> {
//        dodo mock
        val competitionId = "Competition123"
        val categories = categoryRepository.getAllActive(competitionId)

        val routeIds = categories.map { it.routeId }.distinct()
        val backgroundMapIds = categories.map { it.backgroundMapId }.distinct()

        val routesMap = routeFacade.getByIds(routeIds)
        val backgroundMapsMap = backgroundMapFacade.getByIds(backgroundMapIds)

        return categories.map { category ->
            categoryViewMapper.mapToView(
                category = category,
                route = routesMap.find { category.routeId == it.id }!!,
                backgroundMap = backgroundMapsMap.find { category.backgroundMapId == it.id }!!
            )
        }
    }

    fun getActiveById(id: String): Category? {
        return categoryRepository.getActiveById(id)
    }

    fun deactivate(command: DeleteCategoryCommand) {
        val category = categoryRepository.getActiveById(command.categoryId)
        category ?: throw Exception("dodo")

        val updatedCategory = category.copy(
            isActive = false
        )
        categoryRepository.save(updatedCategory)
    }
}