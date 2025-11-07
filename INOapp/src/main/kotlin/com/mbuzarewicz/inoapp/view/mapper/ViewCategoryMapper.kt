package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.domain.model.Category
import com.mbuzarewicz.inoapp.domain.model.Route
import com.mbuzarewicz.inoapp.view.model.CategoryView

class ViewCategoryMapper {
    private val viewBackgroundMapMapper = ViewBackgroundMapMapper()

    fun mapToView(category: Category, route: Route?, backgroundMap: BackgroundMap): CategoryView {
        return CategoryView(
            id = category.id,
            name = category.name,
            routeId = category.routeId,
            routeName = route?.name ?: "---",
            maxTime = category.maxTime,
            backgroundMap = viewBackgroundMapMapper.mapToView(backgroundMap),
        )
    }
}