//package com.mbuzarewicz.inoapp
//
//import org.springframework.stereotype.Service
//import java.nio.file.Files
//import java.nio.file.Path
//import java.nio.file.Paths
//
//@Service
////dodo zrobić z tego port adapter z interfejsem
////dodo backup do usnięcia
//class DodoTilerService {
//
//    fun slice(inputFilePath: String, backgroundMapId: String, minZoom: Int, maxZoom: Int, epsg: String): Path? {
//        val outputDirPatch = Paths.get("/Users/m.buzarewicz/IdeaProjects/INOapp-front/src/assets/maps")
//        val outputDirName = backgroundMapId
//        val outputDir = Paths.get(outputDirPatch.toString(), outputDirName)
//
//        return try {
//            Files.createDirectories(outputDir)
//            val outputDirPath = outputDir.toString()
//
//            val gdal2TilesCommand = arrayOf(
//                "gdal2tiles.py",
//                "--xyz",
//                "-z", "$minZoom-$maxZoom",
//                "--webviewer=none",
//                "--s_srs", "EPSG:$epsg",
//                "-r", "cubicspline",
//                "-p", "mercator",
//                inputFilePath,
//                outputDirPath
//            )
//
//            val process = ProcessBuilder(*gdal2TilesCommand).redirectErrorStream(true).start()
//            val exitCode = process.waitFor()
//
//            if (exitCode == 0) {
//                println("Pomyślnie podzielono plik $inputFilePath na kafelki w $outputDirPath za pomocą gdal2tiles.py")
//                outputDir
//            } else {
//                val error = process.inputStream.bufferedReader().readText()
//                println("Błąd podczas kafelkowania pliku $inputFilePath za pomocą gdal2tiles.py: $error")
//                null
//            }
//        } catch (e: Exception) {
//            println("Wystąpił wyjątek podczas kafelkowania pliku $inputFilePath: ${e.message}")
//            null
//        }
//    }
//}