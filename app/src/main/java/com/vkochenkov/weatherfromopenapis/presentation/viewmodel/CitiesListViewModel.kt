package com.vkochenkov.weatherfromopenapis.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vkochenkov.weatherfromopenapis.App
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.domain.Repository

class CitiesListViewModel: ViewModel() {
    private var repository: Repository = App.instance!!.repository

    private var citiesMutableLiveData = MutableLiveData<List<City>>()

    val citiesLiveData: LiveData<List<City>>
        get()  = citiesMutableLiveData

    fun getAllCitiesFromDb() {
        citiesMutableLiveData.postValue(repository.getAllCitiesFromDb())
    }

    fun getSameNameCitiesFromDb(str: String) {
        citiesMutableLiveData.postValue(repository.getSameNameCitiesFromDb(str))
    }

    fun insertCityToDb(city: City) {
        repository.insertCityToDb(city)
        getAllCitiesFromDb()
    }

    fun deleteCityFromDb(city: City) {
        repository.deleteCityFromDb(city)
        getAllCitiesFromDb()
    }
}