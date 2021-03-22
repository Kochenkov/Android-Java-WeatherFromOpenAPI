package com.vkochenkov.weatherfromopenapis.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vkochenkov.weatherfromopenapis.App
import com.vkochenkov.weatherfromopenapis.data.weather_api.entities.MainResponseObject
import com.vkochenkov.weatherfromopenapis.domain.Repository

class MainScreenViewModel : ViewModel() {

    private var repository: Repository = App.instance!!.repository

    private var weatherMutableLiveData = MutableLiveData<MainResponseObject>()
    private var errorMutableLiveData = MutableLiveData<String>()

    val weatherLiveData: LiveData<MainResponseObject>
        get() = weatherMutableLiveData

    val errorLiveData: LiveData<String>
        get() = errorMutableLiveData

    fun getWeatherFromApi(latitude: String, longitude: String) {
        repository.getWeatherFromApi(
            latitude,
            longitude,
            object : Repository.GetWeatherFromApiCallback {
                override fun onSuccess(weather: MainResponseObject?) {
                    weatherMutableLiveData.postValue(weather)
                }

                override fun onFailure(str: String) {
                    errorMutableLiveData.postValue(str)
                }

            })
    }
}