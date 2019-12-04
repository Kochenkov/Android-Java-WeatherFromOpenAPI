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

   // private CitiesArray citiesArrayObject = new CitiesArray();

    private ArrayList<City> citiesArrayList = objectCitiesArrayLists.createCitiesArrayList();

    private String[] cityNamesArray = new String[citiesArrayList.size()];

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
            cityNamesArray[i] = citiesArrayList.get(i).getName();
        }

        //todo переделать массив на аррей лист
        //передаю массив в адаптер
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityNamesArray);
        setListAdapter(mAdapter);

        //лисенер для поля поиска в тулбаре
        citiesSearchField.addTextChangedListener(watchForCitiesSearchField);
    }

    public void createArrayForAdapter() {

    }

    public void findAndShowCity(String string) {
        ArrayList<String> cityNamesArrayList = new ArrayList<>();

        for (int i = 0; i < citiesArrayList.size(); i++) {
            if (cityNamesArray[i].toLowerCase().contains(string.toLowerCase())) {
                cityNamesArrayList.add(cityNamesArray[i]);
            }
        }
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityNamesArrayList);
        setListAdapter(mAdapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onBackPressed();
        super.onListItemClick(l, v, position, id);




        Toast.makeText(getApplicationContext(), "Вы выбрали город " + (cityNamesArray[position]), Toast.LENGTH_SHORT).show();
        MainActivity.latitudeField.setText(citiesArrayList.get(position).getLatitude());
        MainActivity.longitudeField.setText(citiesArrayList.get(position).getLongitude());
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
