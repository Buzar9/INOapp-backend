package com.mbuzarewicz.inoapp

import com.mbuzarewicz.inoapp.ValidationResult.*

class PersistableValidationResultMapper {

    fun mapToPersistableEntity(domain: ValidationResult): PersistableValidationResult {
        return with(domain) {
            val resultType = when (this) {
                is Pass -> "Pass"
                is Fail -> "Fail"
                is InsufficientData -> "InsufficientData"
            }

            return PersistableValidationResult(
                type = this.type,
                details = this.details,
                resultType = resultType
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableValidationResult): ValidationResult {
        return with(persistable) {
            when (this.resultType) {
                "Pass" -> Pass(
                    type = this.type!!,
                    details = this.details
                )

                "Fail" -> Fail(
                    type = this.type!!,
                    details = this.details
                )

                "InsufficientData" -> InsufficientData(
                    type = this.type!!,
                    details = this.details
                )

                else -> throw IllegalArgumentException("Unknown resultType")
            }
        }
    }
}