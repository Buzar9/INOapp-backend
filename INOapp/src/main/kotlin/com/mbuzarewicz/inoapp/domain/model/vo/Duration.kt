package com.mbuzarewicz.inoapp.domain.model.vo

data class Duration(
//    dodo BigDecimal
    val value: Long,
    val unit: DurationUnit,
) {

    fun convertUnit(targetUnit: DurationUnit): Duration {
        val valueInMilliseconds = when (unit) {
            DurationUnit.MILLISECONDS -> value
            DurationUnit.SECONDS -> value * 1000
            DurationUnit.HOURS -> value * 3600000
        }

        val convertedValue = when (targetUnit) {
            DurationUnit.MILLISECONDS -> valueInMilliseconds
            DurationUnit.SECONDS -> valueInMilliseconds / 1000
            DurationUnit.HOURS -> valueInMilliseconds / 3600000
        }

        return Duration(convertedValue, targetUnit)
    }
}