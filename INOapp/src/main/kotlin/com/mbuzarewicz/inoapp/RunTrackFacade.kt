package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.command.AppendRunTrackPointsCommand
import com.mbuzarewicz.inoapp.domain.service.RunTrackSegmentService
import com.mbuzarewicz.inoapp.domain.service.RunTrackStatsCalculator
import com.mbuzarewicz.inoapp.persistence.repository.DefaultRunRepository
import com.mbuzarewicz.inoapp.persistence.repository.DefaultRunTrackRepository
import com.mbuzarewicz.inoapp.query.GetRunTrackQuery
import com.mbuzarewicz.inoapp.view.mapper.ViewSegmentMapper
import com.mbuzarewicz.inoapp.view.model.RunTrackView
import org.springframework.stereotype.Component

@Component
class RunTrackFacade(
    private val runTrackRepository: DefaultRunTrackRepository,
    private val runRepository: DefaultRunRepository,
) {
    private val statsCalculator = RunTrackStatsCalculator()
    private val segmentService = RunTrackSegmentService()
    private val viewSegmentMapper = ViewSegmentMapper()

    fun uploadRunTrackBatch(command: AppendRunTrackPointsCommand): Map<String, Any> {
        runTrackRepository.appendPoints(command.runId, command.points)

        return mapOf(
            "uploadedCount" to command.points.size,
            "runId" to command.runId
        )
    }

    fun getRunTrack(query: GetRunTrackQuery): RunTrackView? {
        val runId = query.runId
        val run = runRepository.getByRunId(runId) ?: throw IllegalArgumentException("Run with ID $runId not found")
        val runTrack = runTrackRepository.findByRunId(runId) ?: return null
        val duration = run.getMainTime()

        val segments = segmentService.createSegments(runTrack.points)
        val stats = statsCalculator.calculateStats(segments, duration)

        val segmentsView = segments.map { viewSegmentMapper.mapToView(it) }

        return RunTrackView(runId, segmentsView, stats)
    }
}

