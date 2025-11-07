package com.mbuzarewicz.inoapp.query

data class GetFilteredCompetitionResultsQuery(
//    dodo zamienic na pola zamiast kluczy zeby nie wybebeszac bazy danych na zewnatrz i jednoczesnie trzymac to jakos w ryzach
    val filter: Map<String, List<String>>?,
    val pageNumber: Long
)