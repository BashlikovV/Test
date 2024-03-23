package by.bashlikovvv.locationsdata.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.bashlikovvv.locationsdata.local.converters.StringListTypeConverter
import by.bashlikovvv.locationsdata.local.dao.LocationsDao
import by.bashlikovvv.locationsdata.local.model.LocationEntity

@[
    Database(entities = [LocationEntity::class], version = 1)
    TypeConverters(value = [StringListTypeConverter::class])
]
abstract class LocationsDatabase : RoomDatabase() {

    abstract val locationsDao: LocationsDao

}