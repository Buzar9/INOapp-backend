package com.mbuzarewicz.inoapp.command

import com.mbuzarewicz.inoapp.domain.model.Station

data class ChangeRunCategoryCommand(
    val runId: String,
    val categoryId: String,
    val stations: List<Station>? = null
)
