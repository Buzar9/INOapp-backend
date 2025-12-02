package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.CreateCategoryCommand
import com.mbuzarewicz.inoapp.command.DeleteCategoryCommand
import com.mbuzarewicz.inoapp.domain.model.Category
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
            backgroundMapId = route.backgroundMapId
        )
        categoryRepository.save(category)
    }

    fun getStationsByCategoryId(query: GetStationsByCategoryIdQuery): List<GeoView> {
        val category = categoryRepository.getById(query.categoryId) ?: throw Exception()

        val route = routeFacade.getRoute(category.routeId) ?: throw Exception()

        return route.stations.map { geoViewMapper.mapToView(it) }
    }

    fun getAll(): List<CategoryView> {
//        dodo mock
        val competitionId = "Competition123"
        val categories = categoryRepository.getAll(competitionId)
//        dodo nie wiem czy to nie jest kupa, mozna zoptymalizowac strzaly bo przeciez kategorie beda mialy wspolna trase i mape
        return categories.map {
            categoryViewMapper.mapToView(
                category = it,
                route = routeFacade.getRoute(it.routeId),
                backgroundMap = backgroundMapFacade.getById(it.backgroundMapId)!!
            )
        }
    }

    fun getById(id: String): Category? {
        return categoryRepository.getById(id)
    }

    fun getByNames(names: List<String>): List<Category>? {
        return categoryRepository.getByNames(names)
    }

    //    dodo przy usuwaniu, trzeba zrobic tak, żeby oznaczyc ja jako nieaktywną??, żeby wszystko od tego zależne nadal moglo działać
    fun delete(command: DeleteCategoryCommand) {
        categoryRepository.delete(command.categoryId)
    }
}