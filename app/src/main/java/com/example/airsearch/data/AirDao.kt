package com.example.airsearch.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AirDao {

    @Query("SELECT * FROM airports WHERE iata_code LIKE :code OR LOWER(name) LIKE :name")
    fun getAirportSuggestions(code:String,name:String): Flow<List<Airport>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAirport(airport: Airport)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favourite: Favourite)

    @Query("SELECT * FROM favourites")
    fun getFavourites(): Flow<List<Favourite>>

    @Query("SELECT COUNT(*) FROM airports")
    suspend fun getAirportCount(): Int

}