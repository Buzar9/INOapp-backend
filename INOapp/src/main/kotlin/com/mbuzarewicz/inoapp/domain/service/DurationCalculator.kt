package com.mbuzarewicz.inoapp.domain.service

import com.mbuzarewicz.inoapp.domain.model.vo.*
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.HOURS
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.MILLISECONDS
import com.mbuzarewicz.inoapp.domain.model.vo.DurationUnit.SECONDS


class DurationCalculator {
    fun calculateDuration(
        startTime: Long,
        endTime: Long,
        targetUnit: DurationUnit
    ): Duration {
        val durationMillis = endTime - startTime

        val durationValue = when (targetUnit) {
            MILLISECONDS -> durationMillis
            SECONDS -> durationMillis / 1000
            HOURS -> durationMillis / 3600000
        }

        return Duration(value = durationValue, unit = targetUnit)
    }
}



