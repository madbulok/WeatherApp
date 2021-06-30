package com.uzlov.weatherapp.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity
data class ResponseWeather(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val base: String,
//    @Ignore
//    val clouds: Clouds,
    val cod: Int,
//    @Ignore
//    val coord: Coord,
    val dt: Int,
//    @Ignore
//    val main: Main,
    val name: String,
//    @Ignore
//    val rain: Rain ,
//    @Ignore
//    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
//    @Ignore
//    val weather: List<Weather>,
//    @Ignore
//    val wind: Wind
) {
//    constructor() : this(id = 0, base = "", clouds = Clouds(2), cod = 1, coord = Coord(0,0), dt = 0,
//        main = Main(0.0, 0, 0, 0.0, 0.0, 0.0), name = "", rain = Rain(0.0),
//        sys = Sys("", 0, 0, 0, 0), timezone = 0, visibility = 0, weather = emptyList(), wind = Wind(0,0.0, 0.0))
}