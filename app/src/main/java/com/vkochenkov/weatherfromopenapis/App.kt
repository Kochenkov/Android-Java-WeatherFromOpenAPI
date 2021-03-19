package com.vkochenkov.weatherfromopenapis

import android.app.Application
import androidx.room.Room
import com.vkochenkov.weatherfromopenapis.data.db.CitiesDb
import com.vkochenkov.weatherfromopenapis.data.weather_api.WeatherApiService
import com.vkochenkov.weatherfromopenapis.data.weather_api.WeatherApiService.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var apiService: WeatherApiService
    lateinit var database: CitiesDb

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRetrofit()
        initDatabase()
    }

    private fun initRetrofit() {
        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL + BuildConfig.API_ACCESS_KEY + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    private fun initDatabase() {
        database = Room.databaseBuilder(applicationContext, CitiesDb::class.java, "cities_db")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}