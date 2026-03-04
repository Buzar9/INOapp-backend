package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.domain.model.vo.Size
import com.mbuzarewicz.inoapp.domain.model.vo.SizeUnit
import org.springframework.stereotype.Service
import java.io.BufferedOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.util.Comparator
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

@Service
//dodo zrobić z tego port adapter z interfejsem
class TilesSlicerService {

    fun slice(inputFilePath: String, backgroundMapId: String, minZoom: Int, maxZoom: Int, epsg: String): SliceResult? {
        val tempDir = Files.createTempDirectory("tiles-")
        val outputDir = tempDir.resolve(backgroundMapId)

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
                val fileSizeByZoom = calculateFileSizeByZoom(outputDir)

                zipPath = tempDir.resolve("$backgroundMapId.zip")
                Files.deleteIfExists(zipPath)
                zipDirectory(outputDir, zipPath!!)
                println("Pomyślnie utworzono ZIP: $zipPath")
                return SliceResult(zipPath = zipPath, fileSizeByZoom = fileSizeByZoom)
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

    private fun calculateFileSizeByZoom(outputDir: Path): Map<Int, Size> {
        val result = mutableMapOf<Int, Size>()
        Files.list(outputDir).use { dirs ->
            dirs.filter { Files.isDirectory(it) }
                .forEach { zoomDir ->
                    val zoom = zoomDir.fileName.toString().toIntOrNull() ?: return@forEach
                    val sizeInBytes = Files.walk(zoomDir).use { paths ->
                        paths.filter { Files.isRegularFile(it) }
                            .mapToLong { Files.size(it) }
                            .sum()
                    }
                    result[zoom] = Size(sizeInBytes, SizeUnit.BYTES)
                }
        }
        return result
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