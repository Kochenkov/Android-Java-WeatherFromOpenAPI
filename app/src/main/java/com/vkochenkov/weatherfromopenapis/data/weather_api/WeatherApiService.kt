package com.vkochenkov.weatherfromopenapis.data.weather_api

import com.vkochenkov.weatherfromopenapis.data.weather_api.entities.MainResponseObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    companion object {
        const val BASE_URL = "https://api.darksky.net/forecast/" //базовый урл
        const val UNITS = "si" //параметр для получения данных в системе СИ
    }

    @GET("{latitude},{longitude}")
    fun getWeather(
        @Path("latitude") latitude: String?,
        @Path("longitude") longitude: String?,
        @Query("units") units: String?
    ): Call<MainResponseObject>
}