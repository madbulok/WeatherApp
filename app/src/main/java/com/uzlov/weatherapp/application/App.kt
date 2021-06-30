package com.uzlov.weatherapp.application

import android.app.Application
import androidx.room.Room
import com.uzlov.weatherapp.db.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }

    companion object {
        private var appInstance: App? = null
        private const val db_name = "weathers"

        val getWeatherDatabase by lazy {
            Room.databaseBuilder(
                appInstance!!.applicationContext,
                AppDatabase::class.java,
                db_name
            ).build()
        }
    }
}