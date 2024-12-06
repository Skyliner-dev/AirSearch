package com.example.airsearch.ui.screens

import androidx.datastore.preferences.protobuf.Empty
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.airsearch.data.AirRepository
import com.example.airsearch.data.UserSearchRepository
import com.example.airsearch.data.flights
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserPreferencesViewModel(
    private val userSearchRepository: UserSearchRepository,
): ViewModel() {

//    val userPrefUiState: StateFlow<UserPrefUiState> = userSearchRepository.textFieldValue.map {
//        UserPrefUiState(it)
//    }.stateIn(
//        scope = viewModelScope,
//        started = SharingStarted.WhileSubscribed(5_000),
//        initialValue = UserPrefUiState()
//    )


    private var _userPrefUiState = MutableStateFlow(UserPrefUiState())
    val userPrefUiState: StateFlow<UserPrefUiState> = _userPrefUiState.asStateFlow()

    val isPrepopulated: StateFlow<IsPrepopulated> = userSearchRepository.isPrepopulated.map {
        IsPrepopulated(it)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = IsPrepopulated()
    )
    init {
        viewModelScope.launch {
            userSearchRepository.textFieldValue.collect {
                _userPrefUiState.value = UserPrefUiState(it)
            }
        }
    }

   fun updateSearchPreferences(value: String) {
       viewModelScope.launch {
           userSearchRepository.saveTextFieldPreference(value)
       }
   }

    fun updateIsPrepopulatedPreferences(value: Boolean) {
        viewModelScope.launch {
            userSearchRepository.saveIsPrepopulatedPreference(value)
        }
    }






}

data class IsPrepopulated(
    val isPrepopulated: Boolean = false
)

data class UserPrefUiState(
    val textFieldValue: String = "",
)