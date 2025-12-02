package com.mbuzarewicz.inoapp.domain.service

import com.mbuzarewicz.inoapp.domain.model.RunTrackPoint
import com.mbuzarewicz.inoapp.domain.model.vo.DistanceUnit
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.SECONDS
import com.mbuzarewicz.inoapp.domain.model.vo.VelocityUnit.KILOMETERS_PER_HOUR
import com.mbuzarewicz.inoapp.domain.model.RunTrackSegment
import org.springframework.stereotype.Service

@Service
class RunTrackSegmentService(
    private val distanceCalculator: DistanceCalculator = DistanceCalculator(),
    private val durationCalculator: DurationCalculator = DurationCalculator(),
    private val velocityCalculator: VelocityCalculator = VelocityCalculator()
) {

    fun createSegments(points: List<RunTrackPoint>): List<RunTrackSegment> {
        if (points.size < 2) {
            return emptyList()
        }

        return points.windowed(size = 2, step = 1).map { (startPoint, endPoint) ->
            val duration = durationCalculator.calculateDuration(
                startTime = startPoint.timestamp,
                endTime = endPoint.timestamp,
                targetUnit = SECONDS
            )

            val distance = distanceCalculator.calculateDistance(
                firstLocation = startPoint.location,
                secondLocation = endPoint.location,
                targetUnit = DistanceUnit.KILOMETERS
            )

            val velocity = velocityCalculator.calculateVelocity(
                distance = distance,
                duration = duration,
                targetUnit = KILOMETERS_PER_HOUR
            )

            RunTrackSegment(
                startPoint = startPoint.location,
                endPoint = endPoint.location,
                startTime = startPoint.timestamp,
                endTime = endPoint.timestamp,
                distance = distance,
                durationMillis = duration,
                velocity = velocity
            )
        }
    }
}
