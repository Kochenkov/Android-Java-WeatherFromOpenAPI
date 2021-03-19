package com.vkochenkov.weatherfromopenapis.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vkochenkov.weatherfromopenapis.App
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.data.weather_api.entities.MainResponseObject
import com.vkochenkov.weatherfromopenapis.domain.Repository

class CitiesListViewModel: ViewModel() {
    private var repository: Repository = App.instance!!.repository

    private var citiesMutableLiveData = MutableLiveData<List<City>>()

    val citiesLiveData: LiveData<List<City>>
        get()  = citiesMutableLiveData

    fun getAllCities() {
        citiesMutableLiveData.postValue(repository.getAllCitiesFromDb())
    }
}