package com.example.airsearch.ui.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airsearch.data.AirRepository
import com.example.airsearch.data.Airport
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AirportsViewModel(
    private val airportsRepository: AirRepository,
): ViewModel() {

//    var airportUiState: StateFlow<AirportUiState> =
//        airportsRepository.getAirportSuggestions(text.value).map { AirportUiState(it) }
//            .filterNotNull()
//            .stateIn(
//                scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
//                initialValue = AirportUiState()
//            )
    private var _airportUiState = MutableStateFlow(AirportUiState())
    val airportUiState: StateFlow<AirportUiState> = _airportUiState.asStateFlow()
    private var _selectedAirport = MutableStateFlow(SelectedAirport())
    val selectedAirport: StateFlow<SelectedAirport?> = _selectedAirport.asStateFlow()
//
//
    fun getAirportSuggestions(text:String) {
        viewModelScope.launch {
            airportsRepository.getAirportSuggestions(
                code =
                if (text.isEmpty()) "" else
                text.uppercase() + "%",
                name =
                if (text.isEmpty()) "" else
                text.lowercase() + "%"
            ).map { AirportUiState(it) }  .collect {
                _airportUiState.value = _airportUiState.value.copy(airports = it.airports)
            }
        }
    }
    fun insertAirport(airport: Airport) {
        viewModelScope.launch {
            airportsRepository.insertAirport(airport = airport)
        }
    }

    fun selectAirport(airport: Airport) {
        _selectedAirport.value = _selectedAirport.value.copy(airport = airport)
    }

    fun resetSelected() {
        _selectedAirport.value = SelectedAirport()
    }


}
data class SelectedAirport(
    val airport: Airport? = null
)

data class AirportUiState(
    val airports: List<Airport> = emptyList()
)