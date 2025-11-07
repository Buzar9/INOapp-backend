package com.mbuzarewicz.inoapp

import org.springframework.stereotype.Service
import java.io.BufferedOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Comparator
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
//dodo zrobić z tego port adapter z interfejsem
class TilesSlicerService {

    // Bazowy katalog na dane (ustaw w env TILES_OUTPUT_DIR; domyślnie /data/maps)
//    private val outputBaseDir: Path = Paths.get(System.getenv("TILES_OUTPUT_DIR") ?: "/data/maps")

    fun slice(inputFilePath: String, backgroundMapId: String, minZoom: Int, maxZoom: Int, epsg: String): Path? {
        //        dodo mock
        val outputBaseDir = Paths.get("/Users/m.buzarewicz/IdeaProjects/INOapp-front/src/assets/maps")

        val outputDir = outputBaseDir.resolve(backgroundMapId)

        var zipPath: Path? = null

        try {
            Files.createDirectories(outputDir)

            val gdal2TilesCommand = listOf(
                "gdal2tiles.py",
                "--xyz",
                "-z", "$minZoom-$maxZoom",
                "--webviewer=none",
                "--s_srs", "EPSG:$epsg",
                "-r", "cubicspline",
                "-p", "mercator",
                inputFilePath,
                outputDir.toString()
            )

            val process = ProcessBuilder(gdal2TilesCommand)
                .redirectErrorStream(true)
                .start()

            // Czytaj logi, żeby nie zablokować bufora procesu
            val logThread = Thread {
                process.inputStream.bufferedReader().useLines { lines ->
                    lines.forEach { println("[gdal2tiles] $it") }
                }
            }
            logThread.start()

            val exitCode = process.waitFor()
            logThread.join()

            if (exitCode == 0) {
                zipPath = outputBaseDir.resolve("$backgroundMapId.zip")
                Files.deleteIfExists(zipPath)
                zipDirectory(outputDir, zipPath!!)
                println("Pomyślnie utworzono ZIP: $zipPath")
                return zipPath
            } else {
                println("Błąd podczas kafelkowania, kod wyjścia: $exitCode")
                return null
            }
        } catch (e: Exception) {
            println("Wyjątek podczas kafelkowania: ${e.message}")
            return null
        } finally {
            try {
                if (Files.exists(outputDir)) {
                    deleteRecursively(outputDir)
                    println("Wyczyszczono katalog kafelków: $outputDir")
                }
            } catch (cleanupEx: Exception) {
                println("Błąd podczas czyszczenia katalogu kafelków: ${cleanupEx.message}")
            }
        }
    }

    private fun zipDirectory(sourceDir: Path, zipFile: Path) {
        Files.createDirectories(zipFile.parent)
        ZipOutputStream(BufferedOutputStream(Files.newOutputStream(zipFile))).use { zos ->
            Files.walk(sourceDir).use { paths ->
                paths.filter { Files.isRegularFile(it) }.forEach { file ->
                    val entryName = sourceDir.relativize(file).toString().replace(File.separatorChar, '/')
                    zos.putNextEntry(ZipEntry(entryName))
                    Files.newInputStream(file).use { it.copyTo(zos) }
                    zos.closeEntry()
                }
            }
        }
    }

    private fun deleteRecursively(path: Path) {
        if (!Files.exists(path)) return
        Files.walk(path)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.deleteIfExists(it) }
    }
}