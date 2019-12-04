package com.vkochenkov.weatherfromopenapis;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.vkochenkov.weatherfromopenapis.entities.cities.CitiesArrayList;
import com.vkochenkov.weatherfromopenapis.entities.cities.City;

import java.util.ArrayList;

public class CitiesListActivity extends ListActivity {

    private Toolbar toolbar;
    private ArrayAdapter<String> mAdapter;

    private CitiesArrayList objectCitiesArrayLists = new CitiesArrayList();
    private ArrayList<City> citiesArrayList = objectCitiesArrayLists.createCitiesArrayList();
    private ArrayList<String> cityNamesArrayList = new ArrayList<>();
    private ArrayList<String> cityNamesFilteredArrayList = new ArrayList<>();

    private EditText citiesSearchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        toolbar = findViewById(R.id.toolbar_cities_list);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });

        citiesSearchField = findViewById(R.id.edt_city_search_field);

        //делаю массив только с названиями городов, что бы передать его в адаптер
        for (int i = 0; i < citiesArrayList.size(); i++) {
            cityNamesArrayList.add(citiesArrayList.get(i).getName());
        }
        cityNamesFilteredArrayList = cityNamesArrayList;

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityNamesArrayList);
        setListAdapter(mAdapter);

        citiesSearchField.addTextChangedListener(watchForCitiesSearchField);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onBackPressed();
        super.onListItemClick(l, v, position, id);

        String cityName = cityNamesFilteredArrayList.get(position);

        for(int i=0; i<citiesArrayList.size(); i++) {
            if (citiesArrayList.get(i).getName().equals(cityName)) {
                MainActivity.latitudeField.setText(citiesArrayList.get(i).getLatitude());
                MainActivity.longitudeField.setText(citiesArrayList.get(i).getLongitude());
            }
        }

        Toast.makeText(getApplicationContext(),
                "Вы выбрали город " + (cityNamesFilteredArrayList.get(position)),
                Toast.LENGTH_SHORT).show();
    }

    public void findAndShowCity(String string) {
        for (int i = 0; i < citiesArrayList.size(); i++) {
            cityNamesFilteredArrayList.clear();
            if (cityNamesArrayList.get(i).toLowerCase().contains(string.toLowerCase())) {
                cityNamesFilteredArrayList.add(cityNamesArrayList.get(i));
            }
        }
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityNamesFilteredArrayList);
        setListAdapter(mAdapter);
    }

    private TextWatcher watchForCitiesSearchField = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            findAndShowCity(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}


