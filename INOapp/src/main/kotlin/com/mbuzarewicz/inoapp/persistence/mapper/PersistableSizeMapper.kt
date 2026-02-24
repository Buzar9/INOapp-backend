package com.mbuzarewicz.inoapp.persistence.mapper

import com.mbuzarewicz.inoapp.domain.model.vo.Size
import com.mbuzarewicz.inoapp.domain.model.vo.SizeUnit

class PersistableSizeMapper {

    fun mapToPersistableValue(domain: Size): Long {
        return domain.convertUnit(SizeUnit.BYTES).value
    }

    fun mapToDomainEntity(persistableValue: Long): Size {
        return Size(persistableValue, SizeUnit.BYTES)
    }
}
