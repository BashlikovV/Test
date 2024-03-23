package by.bashlikovvv.locationsdata.mapper

import by.bashlikovvv.core.base.IMapper
import by.bashlikovvv.core.domain.model.Image
import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.locationsdata.local.model.LocationEntity

class LocationEntityToLocationMapper(
    private val updateId: Int = 0
) : IMapper<LocationEntity, Location> {
    override fun mapFromEntity(entity: LocationEntity): Location {
        return Location(
            locationName = entity.locationName,
            images = entity.images.map { Image(it) }
        )
    }

    override fun mapToEntity(domain: Location): LocationEntity {
        return LocationEntity(
            id = updateId,
            locationName = domain.locationName,
            images = domain.images.map { it.uri }
        )
    }
}