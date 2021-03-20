package com.vkochenkov.weatherfromopenapis.presentation.recycler

import android.view.View
import android.view.View.OnLongClickListener
import com.vkochenkov.weatherfromopenapis.data.db.entities.City

interface CityClickListener {
    fun onCityClickListener(city: City): View.OnClickListener
    fun onCityLongClickListener(position: Int, city: City): OnLongClickListener
}