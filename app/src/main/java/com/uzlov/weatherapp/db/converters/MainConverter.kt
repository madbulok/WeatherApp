package com.uzlov.weatherapp.db.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.uzlov.weatherapp.model.Clouds
import com.uzlov.weatherapp.model.Main
import com.uzlov.weatherapp.model.Sys

class MainConverter {
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
}