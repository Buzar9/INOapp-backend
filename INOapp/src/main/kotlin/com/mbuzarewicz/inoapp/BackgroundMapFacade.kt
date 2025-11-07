package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AddBackgroundMapCommand
import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.peristance.repository.DefaultBackgroundMapRepository
import com.mbuzarewicz.inoapp.query.GetAllBackgroundMapOptionsQuery
import com.mbuzarewicz.inoapp.query.GetAllBackgroundMapQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewBackgroundMapMapper
import com.mbuzarewicz.inoapp.view.model.BackgroundMapOptionView
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView
import org.springframework.stereotype.Service
import java.nio.file.Files
import java.util.*

@Service
class BackgroundMapFacade(
    private val repository: DefaultBackgroundMapRepository,
    private val tilesSlicerService: TilesSlicerService,
    private val mapMetadataService: MapMetadataService,
    private val storage: ObjectStorage,
) {
    private val viewBackgroundMapMapper = ViewBackgroundMapMapper()

    fun add(command: AddBackgroundMapCommand, filePath: String): String {
        val backgroundMapId = UUID.randomUUID().toString()

        val templateMapMetadata = mapMetadataService.dodoInfo(filePath)!!
        val zipPath = tilesSlicerService.slice(
            filePath,
            backgroundMapId,
            command.minZoom,
            command.maxZoom,
            templateMapMetadata.epsg
        )

//        dodo to jest tak na prawde backgroundMapId ale teoretycznie sliser moze cos zmienic i fasada o tym nie wie
        val objectKey = zipPath!!.fileName.toString()
        val contentType = Files.probeContentType(zipPath) ?: "application/zip"
        val publicUrl = storage.uploadFile(
            file = zipPath,
            key = objectKey,
            contentType = contentType,
            cacheControl = "public, max-age=31536000, immutable"
        )

        try {
            Files.deleteIfExists(zipPath)
            print("Wyczyszczono zipa: $zipPath")
        } catch (e: Exception) {
            println("Nie udało się usunąć pliku ZIP: $zipPath $e")
        }

        val transformedUpperRight = mapMetadataService.transformCoordinates(
            templateMapMetadata.upperRight[1],
            templateMapMetadata.upperRight[0],
            templateMapMetadata.epsg
        )!!

        val transformedLowerRight = mapMetadataService.transformCoordinates(
            templateMapMetadata.lowerRight[1],
            templateMapMetadata.lowerRight[0],
            templateMapMetadata.epsg
        )!!

        val transformedUpperLeft = mapMetadataService.transformCoordinates(
            templateMapMetadata.upperLeft[1],
            templateMapMetadata.upperLeft[0],
            templateMapMetadata.epsg
        )!!

        val transformedLowerLeft = mapMetadataService.transformCoordinates(
            templateMapMetadata.lowerLeft[1],
            templateMapMetadata.lowerLeft[0],
            templateMapMetadata.epsg
        )!!

        val northEast = Location(
            lat = maxOf(transformedUpperRight.lat, transformedUpperLeft.lat),
            lng = maxOf(transformedUpperRight.lng, transformedLowerRight.lng),
            accuracy = 0.0
        )

        val southWest = Location(
            lat = minOf(transformedLowerRight.lat, transformedLowerLeft.lat),
            lng = minOf(transformedUpperLeft.lng, transformedLowerLeft.lng),
            accuracy = 0.0
        )

        val backgroundMap = BackgroundMap(
            id = backgroundMapId,
            name = command.name,
            fileUrl = publicUrl,
            minZoom = command.minZoom,
            maxZoom = command.maxZoom,
            northEast = northEast,
            southWest = southWest,
        )

        repository.save(backgroundMap)

        return backgroundMapId
    }

    fun getBackgroundMapOptions(query: GetAllBackgroundMapOptionsQuery): List<BackgroundMapOptionView> {
        val backgroundMaps = repository.getAll(query.competitionId)
        return backgroundMaps.map { viewBackgroundMapMapper.mapToOptionView(it) }
    }

    fun getAll(query: GetAllBackgroundMapQuery): List<BackgroundMapView> {
        val backgroundMaps = repository.getAll(query.competitionId)
        return backgroundMaps.map { viewBackgroundMapMapper.mapToView(it) }
    }

    fun getById(id: String): BackgroundMap? {
        return repository.getById(id)
    }

    fun getViewById(id: String): BackgroundMapView? {
        val backgroundMap = repository.getById(id) ?: return null
        return viewBackgroundMapMapper.mapToView(backgroundMap)
    }
}