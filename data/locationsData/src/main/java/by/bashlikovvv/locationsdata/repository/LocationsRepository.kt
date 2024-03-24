package by.bashlikovvv.locationsdata.repository

import android.content.SharedPreferences
import by.bashlikovvv.core.domain.model.Location
import by.bashlikovvv.core.domain.repository.ILocationsRepository
import by.bashlikovvv.locationsdata.local.dao.LocationsDao
import by.bashlikovvv.locationsdata.mapper.LocationEntityToLocationMapper

class LocationsRepository(
    private val locationsDao: LocationsDao,
    private val sharedPreferences: SharedPreferences
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

    override suspend fun updateLocation(id: Int, modifier: suspend (Location) -> Location) {
        val updateData = modifier(mapper.mapFromEntity(locationsDao.getLocationById(id)))
        locationsDao.updateLocation(LocationEntityToLocationMapper(id).mapToEntity(updateData))
    }

    override suspend fun checkDataChanged(locations: List<Location>): Boolean {
        val dbLocations = locationsDao.getLocations()
        return when {
            dbLocations.size != locations.size -> true
            dbLocations.map { it.locationName } != locations.map { it.locationName } -> true
            dbLocations.map { it.images.size } != locations.map { it.images.size } -> true
            dbLocations.map { it.images } != locations.map { it.images } -> true

            else -> false
        }
    }

    override suspend fun getSection(): String {
        return sharedPreferences.getString(KEY_SECTION, "") ?: ""
    }

    override suspend fun updateSection(newName: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_SECTION, newName)
            apply()
        }
    }

    companion object {
        const val KEY_SECTION = "section_key"
    }

}