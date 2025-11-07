package com.mbuzarewicz.inoapp.view.mapper

import com.mbuzarewicz.inoapp.domain.model.RuleValidation
import com.mbuzarewicz.inoapp.view.model.RuleValidationView

class ViewRuleValidationMapper {

    fun mapToView(controlPoint: RuleValidation): RuleValidationView {
        return with(controlPoint) {
            RuleValidationView(
//                dodo tłumaczenia
                type.toString(),
                result.toString()
            )
        }
    }
}