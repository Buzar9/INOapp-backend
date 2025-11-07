package com.mbuzarewicz.inoapp.view.model

data class RegisterView(
    val competitions: Map<String, String>,
    val competitionsUnits: Map<String, List<String>>,
)
