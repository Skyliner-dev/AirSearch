package com.example.airsearch

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.airsearch.data.UserSearchRepository
import com.example.airsearch.ui.screens.AirportsViewModel
import com.example.airsearch.ui.screens.FavouriteViewModel
import com.example.airsearch.ui.screens.UserPreferencesViewModel
import kotlinx.coroutines.flow.Flow

object AppViewModelProvider {
    val factory = viewModelFactory {
        initializer {
            AirportsViewModel(airSearchApplication().container.airRepository)
        }
        initializer {
            FavouriteViewModel(airSearchApplication().container.airRepository)
        }
        initializer {
            UserPreferencesViewModel(
                airSearchApplication().userSearchRepository,
            )
        }

    }
}
fun CreationExtras.airSearchApplication(): AirSearchApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as AirSearchApplication)
