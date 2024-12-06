package com.example.airsearch

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.airsearch.data.AppContainer
import com.example.airsearch.data.AppDataContainer
import com.example.airsearch.data.UserSearchRepository

private const val TEXT_PREFERENCE_NAME = "text_preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = TEXT_PREFERENCE_NAME
)

class AirSearchApplication: Application() {
    lateinit var userSearchRepository: UserSearchRepository
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        userSearchRepository = UserSearchRepository(dataStore)
    }

}