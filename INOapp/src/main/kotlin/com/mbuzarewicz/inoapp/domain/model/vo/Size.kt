package com.mbuzarewicz.inoapp.domain.model.vo

data class Size(
    val value: Long,
    val unit: SizeUnit,
) {

    fun convertUnit(targetUnit: SizeUnit): Size {
        val valueInBytes = when (unit) {
            SizeUnit.BYTES -> value
            SizeUnit.KB -> value * 1024
            SizeUnit.MB -> value * 1024 * 1024
            SizeUnit.GB -> value * 1024 * 1024 * 1024
        }

        val convertedValue = when (targetUnit) {
            SizeUnit.BYTES -> valueInBytes
            SizeUnit.KB -> valueInBytes / 1024
            SizeUnit.MB -> valueInBytes / 1024 / 1024
            SizeUnit.GB -> valueInBytes / 1024 / 1024 / 1024
        }

        return Size(convertedValue, targetUnit)
    }
}
