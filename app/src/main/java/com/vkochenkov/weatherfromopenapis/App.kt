package com.vkochenkov.weatherfromopenapis

import android.app.Application
import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiInterface
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class App : Application() {

    lateinit var apiService: WeatherApiInterface

    companion object {
        private const val BASE_URL = "https://api.darksky.net/forecast/"

        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRetrofit()
    }

    private fun initRetrofit() {
        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL + BuildConfig.API_ACCESS_KEY + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiInterface::class.java)
    }

}