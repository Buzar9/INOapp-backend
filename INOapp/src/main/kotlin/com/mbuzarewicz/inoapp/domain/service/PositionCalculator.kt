package com.mbuzarewicz.inoapp.domain.service

import com.mbuzarewicz.inoapp.domain.model.Location
import com.mbuzarewicz.inoapp.domain.model.vo.Distance
import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit
import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit.METERS
import kotlin.math.*
import kotlin.random.Random

class PositionCalculator {

    companion object {
        private const val EARTH_RADIUS_METERS = 6_371_000.0
    }

    fun calculateDistance(
        firstLocation: Location,
        secondLocation: Location,
        targetUnit: DistanceUnit = METERS
    ): Distance {
        val distanceInMeters = calculateDistanceInMeters(
            firstLat = firstLocation.lat,
            firstLng = firstLocation.lng,
            secondLat = secondLocation.lat,
            secondLng = secondLocation.lng
        )

        val distanceValue = when (targetUnit) {
            METERS -> distanceInMeters
            DistanceUnit.KILOMETERS -> distanceInMeters / 1000.0
        }

        return Distance(value = distanceValue, unit = targetUnit)
    }

    private fun calculateDistanceInMeters(
        firstLat: Double,
        firstLng: Double,
        secondLat: Double,
        secondLng: Double
    ): Double {
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

        return EARTH_RADIUS_METERS * centralAngle
    }

    fun calculateTolerance(firstAccuracy: Double, secondAccuracy: Double) = Distance(firstAccuracy + secondAccuracy, METERS)

    fun randomLocationOnCircle(location: Location, radiusMeters: Double): Location {
        val randomAngle = Random.nextDouble(0.0, 2 * Math.PI)

        val deltaLat = (radiusMeters / EARTH_RADIUS_METERS) * cos(randomAngle)
        val deltaLng = (radiusMeters / (EARTH_RADIUS_METERS * cos(Math.toRadians(location.lat)))) * sin(randomAngle)

        return Location(
            lat = location.lat + Math.toDegrees(deltaLat),
            lng = location.lng + Math.toDegrees(deltaLng),
            accuracy = location.accuracy
        )
    }
}