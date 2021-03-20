package com.vkochenkov.weatherfromopenapis.presentation.recycler

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.data.db.entities.City

class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var cityName: TextView

    fun bind(
        itemView: View,
        city: City,
        cityClickListener: CityClickListener,
        position: Int
    ) {
        cityName.text = city.name
        itemView.setOnClickListener(cityClickListener.onCityClickListener(city))
        itemView.setOnLongClickListener(cityClickListener.onCityLongClickListener(position, city))
    }

    init {
        cityName = itemView.findViewById(R.id.item_city_name)
    }
}