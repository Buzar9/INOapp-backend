package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.PenaltyCause.APPLICATION_SWITCHED
import com.mbuzarewicz.inoapp.PenaltyCause.SCANNED_START_IN_ANOTHER_LOCATION
import com.mbuzarewicz.inoapp.TimePenaltyRuleOperation.ADDITION
import com.mbuzarewicz.inoapp.TimePenaltyRuleOperation.MULTIPLICATION

class TimePenaltyPolicy(
    private val timePenaltyRules: Map<PenaltyCause, TimePenaltyRule>,
) {

    fun calculatePenalty(cause: PenaltyCause, offenseValue: String? = null): Long {
        return when (cause) {
            APPLICATION_SWITCHED -> calculateApplicationSwitchedPenalty(offenseValue!!.toLong())
            SCANNED_START_IN_ANOTHER_LOCATION -> calculateScannedStartInAnotherLocationPenalty()
        }
    }

    private fun calculateApplicationSwitchedPenalty(offenseValue: Long): Long {
        val timePenaltyRule = timePenaltyRules[APPLICATION_SWITCHED]!!
        return with(timePenaltyRule) {
            when (operation) {
                MULTIPLICATION -> offenseValue.times(value)
                ADDITION -> offenseValue.plus(value)
            }
        }
    }

    private fun calculateScannedStartInAnotherLocationPenalty(): Long {
        val timePenaltyRule = timePenaltyRules[SCANNED_START_IN_ANOTHER_LOCATION]!!
        return timePenaltyRule.value
    }
}