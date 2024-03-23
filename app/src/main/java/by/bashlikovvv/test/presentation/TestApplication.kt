package by.bashlikovvv.test.presentation

import android.app.Application
import by.bashlikovvv.homescreen.di.HomeScreenComponent
import by.bashlikovvv.homescreen.di.HomeScreenComponentProvider
import by.bashlikovvv.test.di.AppComponent
import by.bashlikovvv.test.di.DaggerAppComponent

class TestApplication
    : Application(),
    HomeScreenComponentProvider {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory()
            .create(this)
    }

    override fun provideHomeScreenComponent(): HomeScreenComponent {
        return appComponent.homeScreenComponent().create()
    }

}