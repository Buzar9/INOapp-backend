package com.mbuzarewicz.inoapp

import kotlin.math.*

class GeoPositionCalculator {

    fun calculateDistance(firstLat: Double, firstLng: Double, secondLat: Double, secondLng: Double): Double {
        val earthRadius = 6371000.0
        val firstRadianLat = Math.toRadians(firstLat)
        val firstRadianLng = Math.toRadians(firstLng)

        val secondRadianLat = Math.toRadians(secondLat)
        val secondRadianLng = Math.toRadians(secondLng)

        val deltaLat = secondRadianLat - firstRadianLat
        val deltaLng = secondRadianLng - firstRadianLng

        val haversine =
            sin(deltaLat / 2).pow(2) + cos(firstRadianLat) * cos(secondRadianLat) * sin(deltaLng / 2).pow(
                2
            )
        val centralAngle = 2 * atan2(sqrt(haversine), sqrt(1 - haversine))

        return earthRadius * centralAngle
    }

    fun calculateTolerance(firstAccuracy: Double, secondAccuracy: Double) = firstAccuracy + secondAccuracy
}