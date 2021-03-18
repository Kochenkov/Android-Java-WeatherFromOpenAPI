package com.vkochenkov.weatherfromopenapis.presentation.recycler;

import android.view.View;

import com.vkochenkov.weatherfromopenapis.data.City;

public interface CityClickListener {
    View.OnClickListener onCityClickListener(City city);

    View.OnLongClickListener onCityLongClickListener(int position, City city);
}
