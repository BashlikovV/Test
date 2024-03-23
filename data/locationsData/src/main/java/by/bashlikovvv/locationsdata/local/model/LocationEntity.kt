package by.bashlikovvv.locationsdata.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import by.bashlikovvv.locationsdata.local.contract.LocationsRoomContract.LocationsTable

@Entity(tableName = LocationsTable.TABLE_NAME)
data class LocationEntity(
    @ColumnInfo(name = LocationsTable.COLUMN_ID)
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = LocationsTable.COLUMN_NAME) val locationName: String,
    @ColumnInfo(name = LocationsTable.COLUMN_IMAGES) val images: List<String>
)