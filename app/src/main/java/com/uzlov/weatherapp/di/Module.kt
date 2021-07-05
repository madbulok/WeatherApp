package com.uzlov.weatherapp.di

import android.app.Application
import androidx.room.Room
import com.uzlov.weatherapp.application.App
import com.uzlov.weatherapp.db.AppDatabase
import com.uzlov.weatherapp.db.WeatherDAO
import com.uzlov.weatherapp.server.WeatherApi
import com.uzlov.weatherapp.viewmodel.WeatherViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit

val viewModule = module {
    single { WeatherViewModel() }
}

val apiService = module {
    fun provideUserApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    single { provideUserApi(get()) }
}

val databaseService = module {
    fun provideDbService(app: Application): AppDatabase {
        return Room.databaseBuilder(
            app,
            AppDatabase::class.java,
            App.db_name
        ).build()
    }

    fun provideDAO(db: AppDatabase): WeatherDAO {
        return db.weatherDAO()
    }

    single { provideDbService(androidApplication()) }
    single { provideDAO(get()) }
}
