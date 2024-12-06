package com.example.airsearch.data

import android.content.Context

interface AppContainer {
    val airRepository: AirRepository
}

class AppDataContainer(private val context: Context): AppContainer {
    override val airRepository: AirRepository by lazy {
        OfflineAirRepository(AirDatabase.getDatabase(context).airDao())
    }
}