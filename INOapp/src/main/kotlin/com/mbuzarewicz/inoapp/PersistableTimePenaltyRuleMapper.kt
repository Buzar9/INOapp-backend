package com.mbuzarewicz.inoapp

class PersistableTimePenaltyRuleMapper {

    fun mapToPersistableEntity(domain: TimePenaltyRule): PersistableTimePenaltyRule {
        return with(domain) {
            PersistableTimePenaltyRule(
                operation = operation.toString(),
                value = value.toString()
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableTimePenaltyRule): TimePenaltyRule {
        return with(persistable) {
            TimePenaltyRule(
                operation = TimePenaltyRuleOperation.valueOf(operation),
                value = value.toLong()
            )
        }
    }
}