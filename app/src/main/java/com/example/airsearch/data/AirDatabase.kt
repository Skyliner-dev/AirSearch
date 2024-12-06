package com.example.airsearch.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Airport::class, Favourite::class], version = 1, exportSchema = false)
abstract class AirDatabase: RoomDatabase() {
    abstract fun airDao(): AirDao

    companion object {
        @Volatile
        private var Instance: AirDatabase? = null

        fun getDatabase(context: Context): AirDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AirDatabase::class.java,
                    "air_database")
                    .build()
                    .also { Instance = it } }
        }
    }
}