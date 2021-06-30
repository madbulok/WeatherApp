package com.uzlov.weatherapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.uzlov.weatherapp.fragments.WeatherFragment
import com.uzlov.weatherapp.model.ResponseWeather

@Database(
    entities = [ResponseWeather::class],
    version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDAO() : WeatherDAO
}