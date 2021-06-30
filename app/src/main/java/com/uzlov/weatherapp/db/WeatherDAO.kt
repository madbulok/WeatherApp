package com.uzlov.weatherapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.uzlov.weatherapp.model.ResponseWeather

@Dao
interface WeatherDAO {
    @Query("SELECT * FROM ResponseWeather")
    fun getAllSavedWeathers() : LiveData<List<ResponseWeather>>

    @Query("SELECT * FROM ResponseWeather WHERE name = :name")
    fun getSavedWeathersByName(name: String) : LiveData<List<ResponseWeather>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(users: ResponseWeather)

    @Delete
    fun delete(user: ResponseWeather)
}