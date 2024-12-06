package com.example.airsearch.data

import kotlinx.coroutines.flow.Flow

interface AirRepository {
    fun getAirportSuggestions(code:String, name:String): Flow<List<Airport>>
    suspend fun insertAirport(airport: Airport)
    suspend fun insertFavourite(favourite: Favourite)
    fun getFavourites(): Flow<List<Favourite>>
    suspend fun getAirportCount(): Int
}

class OfflineAirRepository(private val airDao: AirDao): AirRepository {

    override fun getAirportSuggestions(code: String,name: String): Flow<List<Airport>> = airDao.getAirportSuggestions(code,name)

    override suspend fun insertAirport(airport: Airport) = airDao.insertAirport(airport)

    override suspend fun insertFavourite(favourite: Favourite) = airDao.insertFavourite(favourite)

    override fun getFavourites(): Flow<List<Favourite>> = airDao.getFavourites()

    override suspend fun getAirportCount() = airDao.getAirportCount()
}