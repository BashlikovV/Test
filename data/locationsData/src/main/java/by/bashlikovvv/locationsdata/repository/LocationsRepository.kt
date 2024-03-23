package by.bashlikovvv.locationsdata.repository

import android.net.ConnectivityManager
import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository
import by.bashlikovvv.locationsdata.local.dao.LocationsDao
import by.bashlikovvv.locationsdata.mapper.LocationEntityToLocationMapper

class LocationsRepository(
    private val cm: ConnectivityManager?,
    private val locationsDao: LocationsDao
) : ILocationsRepository {

    private val mapper = LocationEntityToLocationMapper()

    override suspend fun getLocationById(id: Int): Location {
        return mapper.mapFromEntity(locationsDao.getLocationById(id))
    }

    override suspend fun getLocations(): List<Location> {
        return locationsDao.getLocations().map { mapper.mapFromEntity(it) }
    }

    override suspend fun addLocation(location: Location) {
        locationsDao.addLocation(mapper.mapToEntity(location))
    }

    override suspend fun updateLocation(id: Int, modifier: (Location) -> Location) {
        val updateData = modifier(mapper.mapFromEntity(locationsDao.getLocationById(id)))
        locationsDao.updateLocation(LocationEntityToLocationMapper(id).mapToEntity(updateData))
    }

}