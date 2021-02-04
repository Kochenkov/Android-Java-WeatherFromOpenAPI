package com.vkochenkov.weatherfromopenapis;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.entities.cities.CitiesArrayList;
import com.vkochenkov.weatherfromopenapis.recycler.CityListAdapter;

import java.util.ArrayList;

public class CitiesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText citiesSearchField;
    private ArrayAdapter<String> mAdapter;
    private RecyclerView cityListRecycler;

    //private CitiesArrayList objectCitiesArrayLists = new CitiesArrayList();
    //private ArrayList<City> citiesArrayList = new CitiesArrayList().createCitiesArrayList();
    private ArrayList<String> cityNamesArrayList = new ArrayList<>();
    private ArrayList<String> cityNamesFilteredArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        initFields();
        initRecycler();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });


//        //делаю массив только с названиями городов, что бы передать его в адаптер
//        for (int i = 0; i < citiesArrayList.size(); i++) {
//            cityNamesArrayList.add(citiesArrayList.get(i).getName());
//        }
//        cityNamesFilteredArrayList = cityNamesArrayList;
//
//        mAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1,
//                cityNamesArrayList);
//        setListAdapter(mAdapter);
//
//        citiesSearchField.addTextChangedListener(watchForCitiesSearchField);
    }

    private void initRecycler() {
        cityListRecycler.setLayoutManager(new LinearLayoutManager(this));
        cityListRecycler.setAdapter(new CityListAdapter(CitiesArrayList.createCitiesArrayList()));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                                                                                this.getResources().getConfiguration().orientation);
        cityListRecycler.addItemDecoration(dividerItemDecoration);
    }

    private void initFields() {
        toolbar = findViewById(R.id.toolbar_cities_list);
        citiesSearchField = findViewById(R.id.edt_city_search_field);
        cityListRecycler = findViewById(R.id.recycler_city_list);
    }

//    @Override
//    protected void onListItemClick(ListView l, View v, int position, long id) {
//        super.onBackPressed();
//        super.onListItemClick(l, v, position, id);
//
//        String cityName = cityNamesFilteredArrayList.get(position);
//
//        //ищем нужную ячейку по имени выбранного города
//        for (int i = 0; i < citiesArrayList.size(); i++) {
//            if (citiesArrayList.get(i).getName().equals(cityName)) {
//                MainActivity.latitudeField.setText(citiesArrayList.get(i).getLatitude());
//                MainActivity.longitudeField.setText(citiesArrayList.get(i).getLongitude());
//            }
//        }
//
//        Toast.makeText(getApplicationContext(),
//                "Вы выбрали город " + (cityNamesFilteredArrayList.get(position)),
//                Toast.LENGTH_SHORT).show();
//    }

//    public void findAndShowCity(String string) {
//        cityNamesFilteredArrayList = new ArrayList<>();
//
//        for (int i = 0; i < citiesArrayList.size(); i++) {
//            if (cityNamesArrayList.get(i).toLowerCase().contains(string.toLowerCase())) {
//                cityNamesFilteredArrayList.add(cityNamesArrayList.get(i));
//            }
//        }
//
//        mAdapter = new ArrayAdapter<>(this,
//                android.R.layout.simple_list_item_1,
//                cityNamesFilteredArrayList);
//        setListAdapter(mAdapter);
//    }
//
//    private TextWatcher watchForCitiesSearchField = new TextWatcher() {
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before, int count) {
//            findAndShowCity(s.toString());
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//        }
//    };
}


