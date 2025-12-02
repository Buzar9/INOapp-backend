package com.mbuzarewicz.inoapp.domain.service

import com.mbuzarewicz.inoapp.domain.model.RunTrackStats
import com.mbuzarewicz.inoapp.domain.model.vo.Distance
import com.mbuzarewicz.inoapp.domain.model.vo.Duration
import com.mbuzarewicz.inoapp.domain.model.RunTrackSegment

class RunTrackStatsCalculator() {
    private val velocityCalculator = VelocityCalculator()

    fun calculateStats(segments: List<RunTrackSegment>, duration: Duration): RunTrackStats? {
        if (segments.size < 2) {
            return null
        }

        val totalDistance = Distance.sumInUnit(segments.map { it.distance })
        val avgSpeed = velocityCalculator.calculateVelocity(totalDistance, duration)

        return RunTrackStats(
            totalDistance = totalDistance,
            totalDuration = duration,
            averageSpeed = avgSpeed,
        )
    }
}
