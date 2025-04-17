package com.mbuzarewicz.inoapp

class PersistableCheckpointIdMapper {

    fun mapToPersistableEntity(domain: CheckpointId): PersistableCheckpointId {
        return with(domain) {
            PersistableCheckpointId(
                id = id,
                routeName = routeName
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableCheckpointId): CheckpointId {
        return with(persistable) {
            CheckpointId(
                id = id,
                routeName = routeName
            )
        }
    }
}