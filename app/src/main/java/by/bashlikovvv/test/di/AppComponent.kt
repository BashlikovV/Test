package by.bashlikovvv.test.di

import android.app.Application
import by.bashlikovvv.core.di.AppScope
import by.bashlikovvv.core.di.ApplicationQualifier
import by.bashlikovvv.homescreen.di.HomeScreenComponent
import by.bashlikovvv.test.presentation.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@[Component(modules = [DataModule::class, DomainModule::class]) AppScope Singleton]
interface AppComponent {

    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {

        fun create(@[BindsInstance ApplicationQualifier] application: Application): AppComponent

    }

    fun homeScreenComponent(): HomeScreenComponent.Factory

}