package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AddStationCommand
import com.mbuzarewicz.inoapp.domain.model.Station
import com.mbuzarewicz.inoapp.domain.model.StationType
import com.mbuzarewicz.inoapp.peristance.model.DefaultStationRepository
import com.mbuzarewicz.inoapp.view.mapper.ViewGeoMapper
import com.mbuzarewicz.inoapp.view.model.GeoView
import org.springframework.stereotype.Component
import java.util.*

@Component
class StationFacade(
    private val repository: DefaultStationRepository
) {
    private val viewGeoMapper: ViewGeoMapper = ViewGeoMapper()

    //    dodo może wzorcowe stanowisko powinno byc czyms innym niz stanowisko dodane do trasy? jaka nazwe ma miec stanowisko skoro moze byc uzywane w kilku roznych trasach, to podczas tworzenia trasy powinno byc wybierane stanowisko i nadawana mu nazwa. Plus przy tworzeniu stanowiska warto zebrac informacje kto je ustawil i jaki mialo poziom trudnosci. Plus na jakie Competition zostalo utworzone
    fun add(command: AddStationCommand) {
        val station = Station(
            id = UUID.randomUUID().toString(),
            name = command.name,
            type = StationType.valueOf(command.type),
            location = command.location,
            note = command.note
        )

        repository.save(station)
    }

    fun findAllGeoView(): List<GeoView> {
        val stations = repository.findAll()
        return stations.map { viewGeoMapper.mapToView(it) }
    }
}