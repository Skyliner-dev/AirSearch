package com.example.airsearch.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airsearch.data.AirRepository
import com.example.airsearch.data.Favourite
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.Thread.State

class FavouriteViewModel(
    private val favouriteRepository: AirRepository
): ViewModel() {
    val favouritesUiState: StateFlow<FavouriteUiState> =
        favouriteRepository.getFavourites().map { FavouriteUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavouriteUiState())

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    suspend fun addFavourite(favourite: Favourite) {
       favouriteRepository.insertFavourite(favourite)
    }

}


data class FavouriteUiState(
    val favourites: List<Favourite> = emptyList()
)