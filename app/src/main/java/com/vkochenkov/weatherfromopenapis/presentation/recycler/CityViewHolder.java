package com.vkochenkov.weatherfromopenapis.presentation.recycler;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.entities.CityClickListener;
import com.vkochenkov.weatherfromopenapis.R;
import com.vkochenkov.weatherfromopenapis.entities.City;

public class CityViewHolder extends RecyclerView.ViewHolder {

    private TextView cityName;

    public CityViewHolder(View itemView) {
        super(itemView);
        cityName = itemView.findViewById(R.id.item_city_name);
    }

    public void bind(View itemView, City city, CityClickListener cityClickListener, int position) {
        cityName.setText(city.getName());
        itemView.setOnClickListener(cityClickListener.onCityClickListener(city));
        itemView.setOnLongClickListener(cityClickListener.onCityLongClickListener(position, city));
    }
}
