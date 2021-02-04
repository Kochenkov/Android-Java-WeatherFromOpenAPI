package com.vkochenkov.weatherfromopenapis.recycler;


import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.R;
import com.vkochenkov.weatherfromopenapis.entities.cities.City;

public class CityViewHolder extends RecyclerView.ViewHolder {

    private TextView cityName;

    public CityViewHolder(View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.item_city_name);
    }

    public void bind(City city) {
        cityName.setText(city.getName());
    }
}
