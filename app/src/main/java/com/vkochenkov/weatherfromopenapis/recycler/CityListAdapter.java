package com.vkochenkov.weatherfromopenapis.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.R;
import com.vkochenkov.weatherfromopenapis.entities.cities.City;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityViewHolder> {

    List<City> cityList;

    public CityListAdapter(List<City> cityList){
        this.cityList = cityList;
    }

    @NotNull
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CityViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder cityViewHolder, int i) {
        City cityItem = cityList.get(i);
        cityViewHolder.bind(cityItem);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
}
