package com.example.airsearch.data

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class UserSearchRepository(
    private val dataStore: DataStore<Preferences>
) {

    private companion object {
        const val TAG = "UserSearchPreferenceRepo"
        val TEXT_FIELD= stringPreferencesKey("text_field")
        val IS_PREPOPULATED = booleanPreferencesKey("is_prepopulated")
    }
    suspend fun saveTextFieldPreference(value: String) {
        dataStore.edit {
            preferences ->
            preferences[TEXT_FIELD] = value
        }
    }
    suspend fun saveIsPrepopulatedPreference(value: Boolean) {
        dataStore.edit {
            preferences -> preferences[IS_PREPOPULATED] = value
        }
    }
    val isPrepopulated: Flow<Boolean> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading pref boolean", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
            preferences -> preferences[IS_PREPOPULATED] ?: false
        }
    val textFieldValue: Flow<String> = dataStore.data
        .catch {
            if (it is IOException) {
                Log.e(TAG, "Error reading pref", it)
                emit(emptyPreferences())
            } else {
                throw it
            }
        }
        .map {
        preferences -> preferences[TEXT_FIELD] ?: ""
    }
}