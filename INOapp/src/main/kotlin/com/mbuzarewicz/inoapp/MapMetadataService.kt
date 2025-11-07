package com.mbuzarewicz.inoapp

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mbuzarewicz.inoapp.domain.model.Location
import org.springframework.stereotype.Service
import java.io.BufferedReader

@Service
class MapMetadataService() {

    private val mapper = jacksonObjectMapper()

    fun dodoInfo(inputFilePath: String): TemplateMapMetadata? {
        try {
            val gdalInfoCommand = arrayOf(
                "gdalinfo",
                "-json",
                inputFilePath
            )

            val process = ProcessBuilder(*gdalInfoCommand).redirectErrorStream(true).start()
            val output = process.inputStream.bufferedReader().use { it.readText() }
            val exitCode = process.waitFor()

            if (exitCode == 0) {
                println("Pomyślnie podzielono plik $inputFilePath na kafelki w  za pomocą gdal2tiles.py")
                return output.mapToTemplateMapMetadata()
            } else {
                val error = process.inputStream.bufferedReader().readText()
                println("Błąd podczas kafelkowania pliku $inputFilePath za pomocą gdal2tiles.py: $error")
                return null
            }
        } catch (e: Exception) {
            println("Wystąpił wyjątek podczas kafelkowania pliku $inputFilePath: ${e.message}")
            return null
        }
    }

    fun transformCoordinates(lat: Double, lng: Double, sourceEpsg: String): Location? {
        return try {
            val gdalTransformCommand = arrayOf(
                "gdaltransform",
                "-s_srs", "EPSG:$sourceEpsg",
                "-t_srs", "EPSG:4326"
            )

            val process = ProcessBuilder(*gdalTransformCommand).start()

            process.outputStream.bufferedWriter().use { writer ->
                writer.write("$lng $lat\n")
                writer.flush()
            }

            // czytamy wynik z stdout
            val output = process.inputStream.bufferedReader()
                .use(BufferedReader::readText)
                .trim()
                .split(' ')

            return Location(output[1].toDouble(), output[0].toDouble(), 0.0)
        } catch (e: Exception) {
            Location(0.0, 0.0, 0.0)
        }
    }

    private fun String.mapToTemplateMapMetadata(): TemplateMapMetadata {
//        dodo póki co działa tylko z epsg 2180
        val rootNode = mapper.readTree(this)
        val epsg = rootNode.path("stac").path("proj:epsg").toString()
        val upperRight = rootNode.path("cornerCoordinates").path("upperRight").let { listOf(it.get(0).asDouble(), it.get(1).asDouble()) }
        val lowerRight = rootNode.path("cornerCoordinates").path("lowerRight").let { listOf(it.get(0).asDouble(), it.get(1).asDouble()) }
        val upperLeft = rootNode.path("cornerCoordinates").path("upperLeft").let { listOf(it.get(0).asDouble(), it.get(1).asDouble()) }
        val lowerLeft = rootNode.path("cornerCoordinates").path("lowerLeft").let { listOf(it.get(0).asDouble(), it.get(1).asDouble()) }

        return TemplateMapMetadata(
            epsg = epsg,
            upperRight = upperRight,
            lowerRight = lowerRight,
            upperLeft = upperLeft,
            lowerLeft = lowerLeft
        )
    }
}
