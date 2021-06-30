package com.uzlov.weatherapp.server


import com.uzlov.weatherapp.model.ResponseWeather
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    fun getWeatherByLatLng(
        @Query("lat") latitude:String,
        @Query("lon") longitude:String,
        @Query("appid") apikey:String = Constants.appid,
        @Query("units") units:String = "metric",
        @Query("lang") lang: String = "ru",
    ) : Call<ResponseWeather>
}