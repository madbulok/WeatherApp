package com.uzlov.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.uzlov.weatherapp.db.converters.MainConverter

@Entity
@TypeConverters(MainConverter::class)
data class ResponseWeather(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val base: String,
    val clouds: Clouds,
    val cod: Int,
//    @Ignore
//    val coord: Coord,
    val dt: Int,
    val main: Main,
    val name: String,
//    @Ignore
//    val rain: Rain ,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)