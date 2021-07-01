package com.uzlov.weatherapp.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.uzlov.weatherapp.model.*

class MainConverter {

    // for Main
    @TypeConverter
    fun fromMain(main: Main): String {
        return Gson().toJson(main)
    }

    @TypeConverter
    fun toMain(main: String) : Main{
        return Gson().fromJson(main, Main::class.java)
    }

    // for Clouds objects
    @TypeConverter
    fun fromCloud(clouds: Clouds): String {
        return Gson().toJson(clouds)
    }

    @TypeConverter
    fun toClouds(clouds: String) : Clouds {
        return Gson().fromJson(clouds, Clouds::class.java)
    }

    // for Sys object
    @TypeConverter
    fun fromSys(sys: Sys): String {
        return Gson().toJson(sys)
    }

    @TypeConverter
    fun toSys(sys: String) : Sys{
        return Gson().fromJson(sys, Sys::class.java)
    }

    // for Wind object
    @TypeConverter
    fun fromWind(wind: Wind): String {
        return Gson().toJson(wind)
    }

    @TypeConverter
    fun toWind(wind: String) : Wind{
        return Gson().fromJson(wind, Wind::class.java)
    }


    // for Wind object
    @TypeConverter
    fun fromWeather(weathers: List<Weather>): String {
        return Gson().toJson(weathers)
    }

    @TypeConverter
    fun toWeather(weathers: String) : List<Weather>{
        val type = object : TypeToken<List<Weather>>() {}.type
        return Gson().fromJson(weathers, type)
    }
}