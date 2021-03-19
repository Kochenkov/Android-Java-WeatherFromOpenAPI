package com.vkochenkov.weatherfromopenapis.domain

import com.vkochenkov.weatherfromopenapis.App
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.data.db.entities.Coordinates
import com.vkochenkov.weatherfromopenapis.data.weather_api.WeatherApiService.Companion.UNITS

class Repository {

    val dao by lazy { App.instance!!.database.citiesDao() }
    val api by lazy { App.instance?.apiService!! }

    fun getAllCitiesFromDb(): List<City> {
        return dao.getAllCities()
    }

    fun getWeatherFromApi(coordinates: Coordinates) {
        api.getWeather(coordinates.latitude.toString(), coordinates.longitude.toString(), UNITS)
    }
}