package com.mbuzarewicz.inoapp.controller.backoffice

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.mbuzarewicz.inoapp.BackgroundMapFacade
import com.mbuzarewicz.inoapp.command.AddBackgroundMapCommand
import com.mbuzarewicz.inoapp.command.DeleteBackgroundMapCommand
import com.mbuzarewicz.inoapp.query.GetAllBackgroundMapOptionsQuery
import com.mbuzarewicz.inoapp.query.GetAllBackgroundMapQuery
import com.mbuzarewicz.inoapp.view.model.BackgroundMapOptionView
import com.mbuzarewicz.inoapp.view.model.BackgroundMapView
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

@RequestMapping(path = ["/backoffice/background_maps"], produces = [MediaType.APPLICATION_JSON_VALUE])
@RestController
@CrossOrigin(origins = ["http://localhost:4200"])
class BackofficeBackgroundMapController(
    private val backgroundMapFacade: BackgroundMapFacade,
) {

//    gdal2tiles.py --xyz -z 12-16 --processes=4 --webviewer=none --s_srs EPSG:2180 -r cubicspline -p mercator GtTest-rozmiar-500dpi.tif 3857_5

//    curl -X POST -H "Content-Type: multipart/form-data" -F "file=@2180-orginal.tif;type=image/tiff" http://localhost:8080/backoffice/background_maps/upload/geotiff

//    curl -X POST "http://localhost:8080/backoffice/background_maps/add" -H "Content-Type: multipart/form-data" -F "file=@zg_duza.tiff;type=image/tiff" -F 'metadata={"name": "ZG duza z backendu","minZoom": 12,"maxZoom": 14,"competitionId": "comp-123"};type=application/json';

    @PostMapping("/add", consumes = ["multipart/form-data"])
    fun uploadGeoTiff(
        @RequestHeader("X-Competition-Id") competitionId: String,
        @RequestPart("file") file: MultipartFile,
        @RequestPart("metadata") request: AddBackgroundMapRequest
    ): ResponseEntity<String> {
        if (file.isEmpty) {
            return ResponseEntity.badRequest().body("Proszę załączyć plik.")
        }

        if (!file.contentType.equals("image/tiff")) {
            return ResponseEntity.badRequest().body("Dozwolony jest tylko format GeoTIFF (image/tiff).")
        }

        val command = request.toCommand(competitionId)

        val tempDir = Paths.get(System.getProperty("java.io.tmpdir"), "geotiff_uploads")
        Files.createDirectories(tempDir)
        val uploadedFile = File(tempDir.resolve(command.name + "_" + file.originalFilename).toString())

        return try {
            file.transferTo(uploadedFile)
            val filePath = uploadedFile.absolutePath

            val confirmation = backgroundMapFacade.add(command, filePath)
            ResponseEntity.ok(confirmation)
        } catch (e: Exception) {
            e.printStackTrace()
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Wystąpił błąd podczas zapisywania pliku.")
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class AddBackgroundMapRequest(
//        dodo name musi byc przypilnowane jako unikalne w ramach competition
        val name: String,
        val minZoom: Int,
        val maxZoom: Int,
    )

    private fun AddBackgroundMapRequest.toCommand(competitionId: String) = AddBackgroundMapCommand(name, minZoom, maxZoom, competitionId)

    @PostMapping("/options")
    fun getAllOptions(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<List<BackgroundMapOptionView>> {
        val options = backgroundMapFacade.getBackgroundMapOptions(GetAllBackgroundMapOptionsQuery(competitionId))
        return ResponseEntity.status(200).body(options)
    }

    @PostMapping
    fun getAll(
        @RequestHeader("X-Competition-Id") competitionId: String
    ): ResponseEntity<List<BackgroundMapView>> {
        val backgroundMaps = backgroundMapFacade.getAll(GetAllBackgroundMapQuery(competitionId))
        return ResponseEntity.status(200).body(backgroundMaps)
    }

    @PostMapping("/delete")
    fun delete(
        @RequestBody request: DeleteBackgroundMapRequest
    ) {
        backgroundMapFacade.deactivateAndDeleteFile(request.toCommand())
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DeleteBackgroundMapRequest(
        val backgroundMapId: String,
    )

    private fun DeleteBackgroundMapRequest.toCommand() = DeleteBackgroundMapCommand(backgroundMapId)
}
