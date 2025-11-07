package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.domain.model.StationType
import com.mbuzarewicz.inoapp.view.model.DictionaryModel
import org.springframework.stereotype.Component

@Component
class DictionaryFacade {

    fun getStationType(): List<DictionaryModel> {
        return StationType.entries.map {
            DictionaryModel(
                value = it.name,
                label = TranslateService.translate("station-type", it.name)
            )
        }
    }

    fun getStatusType(): List<DictionaryModel> {
        return RunStatus.entries.map {
            DictionaryModel(
                value = it.name,
                label = TranslateService.translate("run-status", it.name)
            )
        }
    }
}