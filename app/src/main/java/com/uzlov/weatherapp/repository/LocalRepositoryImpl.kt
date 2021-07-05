package com.uzlov.weatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.uzlov.weatherapp.db.AppDatabase
import com.uzlov.weatherapp.db.WeatherDAO
import com.uzlov.weatherapp.model.ResponseWeather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

object LocalRepositoryImpl {

    private val database : WeatherDAO = inject(AppDatabase::class.java).value.weatherDAO()

    fun getWeatherByCityName(name: String): LiveData<List<ResponseWeather>> {
        val result = MutableLiveData<List<ResponseWeather>>()

        GlobalScope.launch(Dispatchers.IO) {
            result.postValue(database.getSavedWeathersByName(name))
        }
        return  result
    }

    fun insertWeather(responseWeather: ResponseWeather){
        GlobalScope.launch(Dispatchers.IO) {
            database.insertWeather(responseWeather)
        }
    }
}