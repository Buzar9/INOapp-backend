package com.mbuzarewicz.inoapp

class PersistableCheckpointMapper {
    private val checkpointIdMapper = PersistableCheckpointIdMapper()
    private val locationMapper = PersistableLocationMapper()

    fun mapToPersistableEntity(domain: Checkpoint): PersistableCheckpoint {
        return with(domain) {
            PersistableCheckpoint(
                checkpointId = checkpointIdMapper.mapToPersistableEntity(checkpointId),
                location = locationMapper.mapToPersistableEntity(location),
                timestamp = timestamp
            )
        }
    }

    fun mapToDomainEntity(persistable: PersistableCheckpoint): Checkpoint {
        return with(persistable) {
            Checkpoint(
                checkpointId = checkpointIdMapper.mapToDomainEntity(checkpointId),
                location = locationMapper.mapToDomainEntity(location),
                timestamp = timestamp
            )
        }
    }
}