package by.bashlikovvv.test.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import by.bashlikovvv.core.di.ApplicationQualifier
import by.bashlikovvv.core.domain.repository.ILocationsRepository
import by.bashlikovvv.core.ext.preLoadData
import by.bashlikovvv.locationsdata.local.LocationsDatabase
import by.bashlikovvv.locationsdata.local.contract.LocationsRoomContract
import by.bashlikovvv.locationsdata.local.dao.LocationsDao
import by.bashlikovvv.locationsdata.repository.LocationsRepository
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import dagger.Module
import dagger.Provides

@Module
class DataModule {

    @Provides
    fun provideLocationsDatabase(
        @ApplicationQualifier application: Application
    ): LocationsDatabase {
        val sql = application.assets.open("db_init.sql")
            .bufferedReader()
            .use { it.readText() }
            .split(';')
            .first { it.isNotBlank() }

        return Room
            .databaseBuilder(
                context = application,
                klass = LocationsDatabase::class.java,
                name = LocationsRoomContract.DATABASE_NAME
            )
            .preLoadData(sql)
            .build()
    }

    @Provides
    fun provideApplicationSharedPrefs(
        @ApplicationQualifier application: Application
    ): SharedPreferences {
        val prefs = application.getSharedPreferences("APPLICATION_SP", Context.MODE_PRIVATE)
        if (prefs.all.isEmpty()) {
            with(prefs.edit()) {
                putString("section_key", application.getString(by.bashlikovvv.core.R.string.streets))
                apply()
            }
        }

        return prefs
    }

    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return Firebase.storage("gs://test-5f600.appspot.com")
    }

    @Provides
    fun provideLocationsDao(
        db: LocationsDatabase
    ): LocationsDao {
        return db.locationsDao
    }

    @Provides
    fun provideLocationsRepository(
        locationsDao: LocationsDao,
        sharedPreferences: SharedPreferences
    ): ILocationsRepository {
        return LocationsRepository(locationsDao, sharedPreferences)
    }

}