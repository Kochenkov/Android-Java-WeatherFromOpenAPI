package com.vkochenkov.weatherfromopenapis;

import android.view.View;

import com.vkochenkov.weatherfromopenapis.entities.cities.City;

public interface CityClickListener {
    View.OnClickListener onCityClickListener(City city);
}
