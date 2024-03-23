package by.bashlikovvv.homescreen.di

import by.bashlikovvv.core.di.Feature
import by.bashlikovvv.homescreen.presentation.ui.IncomeFragment
import by.bashlikovvv.homescreen.presentation.ui.LocationsFragment
import by.bashlikovvv.homescreen.presentation.ui.MoodBoardFragment
import by.bashlikovvv.homescreen.presentation.ui.SettingsFragment
import dagger.Subcomponent
import javax.inject.Scope

@Scope
annotation class HomeScreenScope

@[Feature HomeScreenScope Subcomponent]
interface HomeScreenComponent {

    fun inject(locationsFragment: LocationsFragment)

    fun inject(incomeFragment: IncomeFragment)

    fun inject(moodBoardFragment: MoodBoardFragment)

    fun inject(settingsFragment: SettingsFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(): HomeScreenComponent
    }

}