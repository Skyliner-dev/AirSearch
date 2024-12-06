package com.example.airsearch.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val departureCode: String, //iataCode
    val destinationCode:String //iataCode
)
