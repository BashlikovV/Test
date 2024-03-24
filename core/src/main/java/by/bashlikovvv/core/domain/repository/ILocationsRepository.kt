package by.bashlikovvv.core.domain.repository

import by.bashlikovvv.core.domain.model.Location

interface ILocationsRepository {

    suspend fun getLocationById(id: Int): Location

    suspend fun getLocations(): List<Location>

    suspend fun addLocation(location: Location)

    suspend fun updateLocation(id: Int, modifier: suspend (Location) -> Location)

    suspend fun checkDataChanged(locations: List<Location>): Boolean

}