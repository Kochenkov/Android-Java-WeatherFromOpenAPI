package com.vkochenkov.weatherfromopenapis.data.weather_api

import com.vkochenkov.weatherfromopenapis.data.weather_api.dto.MainResponseObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApiService {

    companion object {
        const val BASE_URL = "https://api.darksky.net/forecast/"
    }

    @GET("{latitude},{longitude}")
    fun getWeather(
        @Path("latitude") latitude: String?,
        @Path("longitude") longitude: String?,
        @Query("units") units: String?
    ): Call<MainResponseObject?>?
}