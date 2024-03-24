package by.bashlikovvv.locationsdata.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import by.bashlikovvv.locationsdata.local.contract.LocationsRoomContract.LocationsTable
import by.bashlikovvv.locationsdata.local.model.LocationEntity

@Dao
interface LocationsDao {

    @Query(
        """SELECT * 
           FROM ${LocationsTable.TABLE_NAME} 
           WHERE ${LocationsTable.COLUMN_ID} = :id;"""
    )
    suspend fun getLocationById(id: Int): LocationEntity

    @Query(
        """SELECT * 
           FROM ${LocationsTable.TABLE_NAME};"""
    )
    suspend fun getLocations(): List<LocationEntity>

    @Insert(
        entity = LocationEntity::class,
        onConflict = OnConflictStrategy.REPLACE
    )
    suspend fun addLocation(locationEntity: LocationEntity)

    @Update(entity = LocationEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(locationEntity: LocationEntity)

}