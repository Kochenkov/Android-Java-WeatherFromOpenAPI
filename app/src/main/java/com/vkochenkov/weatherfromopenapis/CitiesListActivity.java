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

public class CitiesListActivity extends ListActivity {

    private Toolbar toolbar;
    private ArrayAdapter<String> mAdapter;

    CitiesArray citiesArrayObject = new CitiesArray();

    String[][] citiesArray = citiesArrayObject.createCitiesArray();
    String[] cityNamesArray = new String[citiesArray.length];
    EditText citiesSearchField;

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
        for(int i=0; i<citiesArray.length; i++) {
            cityNamesArray[i] = citiesArray[i][0];
        }

        //передаю массив в адаптер
        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityNamesArray);

        setListAdapter(mAdapter);
        citiesSearchField.addTextChangedListener(watchForCitiesSearchField);

    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onBackPressed();
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getApplicationContext(),
                "Вы выбрали город " + (cityNamesArray[position]), Toast.LENGTH_SHORT).show();
        MainActivity.latitudeField.setText(citiesArray[position][1]);
        MainActivity.longitudeField.setText(citiesArray[position][2]);
    }

    private TextWatcher watchForCitiesSearchField = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
