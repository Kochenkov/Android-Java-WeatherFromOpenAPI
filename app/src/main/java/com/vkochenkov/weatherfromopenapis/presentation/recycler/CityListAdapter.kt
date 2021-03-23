package com.vkochenkov.weatherfromopenapis.presentation.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.data.db.entities.City

class CityListAdapter(var cityClickListener: CityClickListener) : RecyclerView.Adapter<CityViewHolder>() {

    private var cityList: List<City> = ArrayList()

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CityViewHolder {
        return CityViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_city, viewGroup, false)
        )
    }

    override fun onBindViewHolder(cityViewHolder: CityViewHolder, i: Int) {
        val cityItem = cityList[i]
        cityViewHolder.bind(cityViewHolder.itemView, cityItem, cityClickListener, i)
    }

    override fun getItemCount(): Int {
        return cityList.size
    }

    fun setData(cityList: List<City>) {
        this.cityList = cityList
        notifyDataSetChanged()
    }
}