package com.vkochenkov.weatherfromopenapis.domain

import com.vkochenkov.weatherfromopenapis.data.weather_api.entities.MainResponseObject

class ApiResult {
    var success: MainResponseObject? = null
    var error: String? = null

    override fun toString(): String {
        return "$success, $error"
    }
}