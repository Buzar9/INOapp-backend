package com.mbuzarewicz.inoapp.peristance.mapper

import com.mbuzarewicz.inoapp.domain.model.RuleType
import com.mbuzarewicz.inoapp.domain.model.RuleValidation
import com.mbuzarewicz.inoapp.domain.model.RuleValidationResult
import com.mbuzarewicz.inoapp.peristance.model.PersistableRuleValidation

class PersistableRuleValidationtMapper {

    fun mapToPersistableEntity(domain: RuleValidation): PersistableRuleValidation {
        return with(domain) {
            PersistableRuleValidation(
                type.toString(),
                result.toString()
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableRuleValidation): RuleValidation {
        return with(persistable) {
            RuleValidation(
                RuleType.valueOf(type),
                RuleValidationResult.valueOf(result)
            )
        }
    }
}