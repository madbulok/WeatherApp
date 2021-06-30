package com.uzlov.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.uzlov.weatherapp.model.ResponseWeather
import com.uzlov.weatherapp.server.Constants
import com.uzlov.weatherapp.server.WeatherApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RemoteRepositoryImpl {

    private val weatherApi = Retrofit
        .Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(
            GsonConverterFactory.create(
            GsonBuilder().setLenient().create()
        )
        )
        .build().create(WeatherApi::class.java)

    fun loadWeatherByLatLng(lat: Double, lng: Double) : LiveData<ResponseWeather> {
        val responseResult = MutableLiveData<ResponseWeather>()

        weatherApi.getWeatherByLatLng(lat.toString(),
            lng.toString(),
            Constants.appid
        ).enqueue(object  : Callback<ResponseWeather>{
            override fun onResponse(
                call: Call<ResponseWeather>,
                response: Response<ResponseWeather>
            ) {
                if (response.isSuccessful){
                    responseResult.value = response.body()
                    Log.e("TAG", response.code().toString())
                } else {
                    Log.e("TAG", response.code().toString())
                }
            }

            override fun onFailure(call: Call<ResponseWeather>, t: Throwable) {
                Log.e("TAG", t.message.toString())
            }
        })

        return responseResult
    }

}