package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.PenaltyCause.APPLICATION_SWITCHED
import com.mbuzarewicz.inoapp.PenaltyCause.SCANNED_START_IN_ANOTHER_LOCATION
import com.mbuzarewicz.inoapp.TimePenaltyRuleOperation.ADDITION
import com.mbuzarewicz.inoapp.TimePenaltyRuleOperation.MULTIPLICATION

//dodo factory
class RunPenaltyPolicyFactory {

    //    w przyszlosci do wyciagniecia z db
    private val defaultTimePenaltyRules = mapOf(
        APPLICATION_SWITCHED to TimePenaltyRule(MULTIPLICATION, 2L),
        SCANNED_START_IN_ANOTHER_LOCATION to TimePenaltyRule(ADDITION, 60L)
    )
    private val extremeTimePenaltyRules = mapOf(
        APPLICATION_SWITCHED to TimePenaltyRule(MULTIPLICATION, 10L),
        SCANNED_START_IN_ANOTHER_LOCATION to TimePenaltyRule(ADDITION, 300L)
    )
    private val wyjadaczeTimePenaltyRules = mapOf(
        APPLICATION_SWITCHED to TimePenaltyRule(MULTIPLICATION, 5L),
        SCANNED_START_IN_ANOTHER_LOCATION to TimePenaltyRule(ADDITION, 150L)
    )

    fun resolve(routeName: String): TimePenaltyPolicy {
        return when (routeName) {
            "EXTREME" -> TimePenaltyPolicy(extremeTimePenaltyRules)
            "WYJADACZE" -> TimePenaltyPolicy(wyjadaczeTimePenaltyRules)
            else -> TimePenaltyPolicy(defaultTimePenaltyRules)
        }
    }
}