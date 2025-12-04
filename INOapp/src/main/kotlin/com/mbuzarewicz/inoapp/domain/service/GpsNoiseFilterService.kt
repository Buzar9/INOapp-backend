package com.mbuzarewicz.inoapp.domain.service

import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.domain.model.RunTrackPoint
import org.springframework.stereotype.Service
import kotlin.math.*

@Service
class GpsNoiseFilterService {

    // Zahardcodowane parametry konfiguracji
    private val ACCURACY_THRESHOLD = 120.0  // metry
    private val OUTLIER_STD_DEV = 3.0       // współczynnik odchylenia standardowego
    private val PROCESS_NOISE = 2.0         // szum procesu dla Kalmana
    private val WINDOW_SIZE = 5             // okno dla outlier detection

    fun filterPoints(points: List<RunTrackPoint>): List<RunTrackPoint> {
        if (points.size < 2) return points

        return points
            .let { filterByAccuracy(it) }
            .let { removeStatisticalOutliers(it) }
            .let { applyKalmanFilter(it) }
    }

    private fun filterByAccuracy(points: List<RunTrackPoint>): List<RunTrackPoint> {
        return points.filter { point ->
            point.location.accuracy <= ACCURACY_THRESHOLD
        }
    }

    private fun removeStatisticalOutliers(points: List<RunTrackPoint>): List<RunTrackPoint> {
        if (points.size < WINDOW_SIZE) return points

        val filtered = mutableListOf<RunTrackPoint>()

        for (i in points.indices) {
            val windowStart = maxOf(0, i - WINDOW_SIZE / 2)
            val windowEnd = minOf(points.size, i + WINDOW_SIZE / 2 + 1)
            val window = points.subList(windowStart, windowEnd)

            // Oblicz średnią pozycję w oknie
            val meanLat = window.map { it.location.lat }.average()
            val meanLng = window.map { it.location.lng }.average()

            // Oblicz odchylenie standardowe w oknie
            val stdDevLat = calculateStdDev(window.map { it.location.lat }, meanLat)
            val stdDevLng = calculateStdDev(window.map { it.location.lng }, meanLng)

            // Sprawdź czy punkt mieści się w granicach
            val point = points[i]
            val latDiff = abs(point.location.lat - meanLat)
            val lngDiff = abs(point.location.lng - meanLng)

            val isInBounds = latDiff <= OUTLIER_STD_DEV * stdDevLat &&
                           lngDiff <= OUTLIER_STD_DEV * stdDevLng

            if (isInBounds) {
                filtered.add(point)
            }
        }

        return filtered
    }


    private fun applyKalmanFilter(points: List<RunTrackPoint>): List<RunTrackPoint> {
        if (points.isEmpty()) return points

        val filtered = mutableListOf<RunTrackPoint>()

        // Inicjalizacja stanu Kalmana pierwszym punktem
        var state = KalmanState(
            lat = points[0].location.lat,
            lng = points[0].location.lng,
            uncertainty = points[0].location.accuracy
        )

        filtered.add(points[0])

        // Przetwarzanie kolejnych punktów
        for (i in 1 until points.size) {
            val measurement = points[i]

            // Predykcja: zakładamy brak ruchu (tylko pozycja, bez prędkości)
            val predictedLat = state.lat
            val predictedLng = state.lng
            val predictedUncertainty = state.uncertainty + PROCESS_NOISE

            // Measurement noise z accuracy punktu GPS
            val measurementNoise = measurement.location.accuracy

            // Kalman Gain
            val kalmanGain = predictedUncertainty / (predictedUncertainty + measurementNoise)

            // Update: łączenie predykcji z pomiarem
            val updatedLat = predictedLat + kalmanGain * (measurement.location.lat - predictedLat)
            val updatedLng = predictedLng + kalmanGain * (measurement.location.lng - predictedLng)
            val updatedUncertainty = (1 - kalmanGain) * predictedUncertainty

            // Aktualizacja stanu
            state = KalmanState(updatedLat, updatedLng, updatedUncertainty)

            // Utworzenie wygładzonego punktu
            val smoothedPoint = RunTrackPoint(
                timestamp = measurement.timestamp,
                location = Location(
                    lat = updatedLat,
                    lng = updatedLng,
                    accuracy = measurement.location.accuracy // zachowujemy oryginalną accuracy
                )
            )

            filtered.add(smoothedPoint)
        }

        return filtered
    }

    private fun calculateStdDev(values: List<Double>, mean: Double): Double {
        if (values.size < 2) return 0.0

        val variance = values.map { (it - mean).pow(2) }.average()
        return sqrt(variance)
    }

    private data class KalmanState(
        val lat: Double,
        val lng: Double,
        val uncertainty: Double
    )
}
