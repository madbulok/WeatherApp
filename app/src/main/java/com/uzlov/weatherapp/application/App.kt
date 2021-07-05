package com.uzlov.weatherapp.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import androidx.multidex.MultiDex
import com.uzlov.weatherapp.db.AppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import com.uzlov.weatherapp.di.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        appInstance = this
        startKoin {
            androidContext(this@App)
            androidLogger(Level.DEBUG)
            modules(listOf(viewModule, databaseService))
        }
    }

    companion object {
        private var appInstance: App? = null
        const val db_name = "weathers"

        val getWeatherDatabase by lazy {
            Room.databaseBuilder(
                appInstance!!.applicationContext,
                AppDatabase::class.java,
                db_name
            ).build()
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}