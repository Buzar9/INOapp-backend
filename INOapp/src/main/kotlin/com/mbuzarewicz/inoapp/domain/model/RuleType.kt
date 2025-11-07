package com.mbuzarewicz.inoapp.domain.model

enum class RuleType {
    IS_WITHIN_TOLERANCE_RANGE,
    FINISHED_WITHIN_TIME_LIMIT,
    INTERVAL_IS_LONG_ENOUGH,

    //    deprecated
    SCAN_INTERVAL_IS_LONG_ENOUGH
}