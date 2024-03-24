package by.bashlikovvv.test.di

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import by.bashlikovvv.core.di.AppScope
import by.bashlikovvv.core.di.ApplicationQualifier
import by.bashlikovvv.core.domain.repository.ILocationsRepository
import by.bashlikovvv.core.domain.usecase.AddLocationUseCase
import by.bashlikovvv.core.domain.usecase.CheckLocationsDataChangedUseCase
import by.bashlikovvv.core.domain.usecase.GetLocationByIdUseCase
import by.bashlikovvv.core.domain.usecase.GetLocationsUseCase
import by.bashlikovvv.core.domain.usecase.GetStringUseCase
import by.bashlikovvv.core.domain.usecase.UpdateLocationUseCase
import by.bashlikovvv.core.domain.usecase.UploadImageUseCase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides

@Module
class DomainModule {

    @[Provides AppScope]
    fun provideGetStringUseCase(
        @ApplicationQualifier application: Application
    ): GetStringUseCase {
        return GetStringUseCase(application)
    }

    @[Provides AppScope]
    fun provideGetLocationByIdUseCase(
        locationsRepository: ILocationsRepository
    ): GetLocationByIdUseCase {
        return GetLocationByIdUseCase(locationsRepository)
    }

    @[Provides AppScope]
    fun provideGetLocationsUseCase(
        locationsRepository: ILocationsRepository
    ): GetLocationsUseCase {
        return GetLocationsUseCase(locationsRepository)
    }

    @[Provides AppScope]
    fun provideAddLocationUseCase(
        locationsRepository: ILocationsRepository
    ): AddLocationUseCase {
        return AddLocationUseCase(locationsRepository)
    }

    @[Provides AppScope]
    fun provideUploadImageUseCase(
        @ApplicationQualifier application: Application,
        firebaseStorage: FirebaseStorage
    ): UploadImageUseCase {
        val connectivityManager = application
            .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?

        return UploadImageUseCase(firebaseStorage, connectivityManager)
    }

    @[Provides AppScope]
    fun provideUpdateLocationUseCase(
        locationsRepository: ILocationsRepository
    ): UpdateLocationUseCase {
        return UpdateLocationUseCase(locationsRepository)
    }

    @[Provides AppScope]
    fun provideCheckLocationsDataChangedUseCase(
        locationsRepository: ILocationsRepository
    ): CheckLocationsDataChangedUseCase {
        return CheckLocationsDataChangedUseCase(locationsRepository)
    }

}