package com.vkochenkov.weatherfromopenapis.recycler;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.R;
import com.vkochenkov.weatherfromopenapis.db.DBHelper;
import com.vkochenkov.weatherfromopenapis.entities.City;
import com.vkochenkov.weatherfromopenapis.entities.CityClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityViewHolder> {

    SQLiteDatabase database;
    List<City> cityList = new ArrayList<>();
    CityClickListener cityClickListener;

    public CityListAdapter(SQLiteDatabase database, CityClickListener cityClickListener) {
        this.cityClickListener = cityClickListener;
        this.database = database;
        updateDataListFromDb();
    }

    @NotNull
    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CityViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_city, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(CityViewHolder cityViewHolder, int i) {
        final City cityItem = cityList.get(i);
        cityViewHolder.bind(cityViewHolder.itemView, cityItem, cityClickListener, i);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public void updateDataListFromDb() {
        cityList = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.CITIES_TABLE,
                                       null,
                                       null,
                                       null,
                                       null,
                                       null,
                                       null);

        if (cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int latitudeIndex = cursor.getColumnIndex(DBHelper.KEY_LATITUDE);
            int longitudeIndex = cursor.getColumnIndex(DBHelper.KEY_LONGITUDE);

            do {
                cityList.add(new City(cursor.getString(nameIndex), cursor.getString(latitudeIndex), cursor.getString(longitudeIndex)));
            } while (cursor.moveToNext());

        }
        cursor.close();
    }

    public void findAndShowCityItems(String str) {
        updateDataListFromDb();

        List<City> newCityList = new ArrayList<>();
            for (int i=0; i<cityList.size(); i++) {
                if (cityList.get(i).getName().toLowerCase().contains(str.toLowerCase())) {
                    newCityList.add(cityList.get(i));
                }
            }
            cityList = newCityList;

        notifyDataSetChanged();
    }
}
