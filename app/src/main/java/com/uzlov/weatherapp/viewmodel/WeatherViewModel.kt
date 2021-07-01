package com.uzlov.weatherapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.uzlov.weatherapp.RemoteRepositoryImpl
import com.uzlov.weatherapp.model.ResponseWeather

class WeatherViewModel : ViewModel() {

    fun getWeatherByLatLng(lat: Double, lng: Double) : LiveData<ResponseWeather>{
        return RemoteRepositoryImpl.loadWeatherByLatLng(lat, lng)
    }
}
