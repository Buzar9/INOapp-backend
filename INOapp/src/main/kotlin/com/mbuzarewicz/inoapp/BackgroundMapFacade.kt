package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AddBackgroundMapCommand
import com.mbuzarewicz.inoapp.command.DeleteBackgroundMapCommand
import com.mbuzarewicz.inoapp.domain.model.BackgroundMap
import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.peristance.repository.DefaultBackgroundMapRepository
import com.mbuzarewicz.inoapp.query.GetAllBackgroundMapOptionsQuery
import com.mbuzarewicz.inoapp.query.GetAllBackgroundMapQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewBackgroundMapMapper
import com.mbuzarewicz.inoapp.view.model.BackgroundMapOptionView
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView
import org.slf4j.LoggerFactory
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
    private val logger = LoggerFactory.getLogger(BackgroundMapFacade::class.java)

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

        storage.uploadFile(
            file = zipPath,
            key = objectKey,
            contentType = contentType,
            cacheControl = "public, max-age=31536000, immutable"
        )

        val fileSize = Files.size(zipPath) / 1024 / 1024

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
            fileSize = fileSize,
            minZoom = command.minZoom,
            maxZoom = command.maxZoom,
            northEast = northEast,
            southWest = southWest,
            isActive = true,
        )

        repository.save(backgroundMap)

        return backgroundMapId
    }

    fun deactivateAndDeleteFile(command: DeleteBackgroundMapCommand): Boolean {
        val backgroundMapId = command.backgroundMapId
        val backgroundMap = repository.getById(backgroundMapId) ?: run {
            logger.error("BackgroundMap with id $backgroundMapId not found")
            return false
        }

        val key = "$backgroundMapId.zip"
        val deleted = try {
            storage.deleteFile(key)
        } catch (e: Exception) {
            logger.error("Error while deleting file for $backgroundMapId: ${e.message}", e)
            false
        }

        val updated = backgroundMap.copy(isActive = false)
        repository.save(updated)

        if (deleted) {
            logger.info("Successfully deleted storage file and deactivated BackgroundMap $backgroundMapId")
        } else {
            logger.error("Failed to delete storage file for BackgroundMap $backgroundMapId, but record was deactivated")
        }

        return deleted
    }

    fun getBackgroundMapOptions(query: GetAllBackgroundMapOptionsQuery): List<BackgroundMapOptionView> {
        val backgroundMaps = repository.getAll(query.competitionId)
        return backgroundMaps.map { viewBackgroundMapMapper.mapToOptionView(it) }
    }

    fun getAll(query: GetAllBackgroundMapQuery): List<BackgroundMapView> {
        val backgroundMaps = repository.getAll(query.competitionId)
        return backgroundMaps.filter { it.isActive }
                                .map { viewBackgroundMapMapper.mapToView(it) }
    }

    fun getById(id: String): BackgroundMap? {
        return repository.getById(id)
    }

    fun getViewById(id: String): BackgroundMapView? {
        val backgroundMap = repository.getById(id) ?: return null
        return viewBackgroundMapMapper.mapToView(backgroundMap)
    }
}