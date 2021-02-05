package com.vkochenkov.weatherfromopenapis;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.db.DBHelper;
import com.vkochenkov.weatherfromopenapis.entities.City;
import com.vkochenkov.weatherfromopenapis.entities.CityClickListener;
import com.vkochenkov.weatherfromopenapis.recycler.CityListAdapter;

public class CitiesListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText citiesSearchField;
    private RecyclerView cityListView;
    private Button addCityBtn;

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResumed", "onResumed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        initFields();
        initRecycler(database);

        setAddCityBtnClickListener(database);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initRecycler(SQLiteDatabase database) {
        cityListView.setLayoutManager(new LinearLayoutManager(this));
        CityClickListener cityClickListener = new CityClickListener() {
            @Override
            public View.OnClickListener onCityClickListener(final City city) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo нужно переписать
                        MainActivity.latitudeField.setText(city.getLatitude());
                        MainActivity.longitudeField.setText(city.getLongitude());
                        onBackPressed();
                    }
                };
            }
        };
        cityListView.setAdapter(new CityListAdapter(database, cityClickListener));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                                                                                this.getResources().getConfiguration().orientation);
        cityListView.addItemDecoration(dividerItemDecoration);
    }

    private void initFields() {
        toolbar = findViewById(R.id.toolbar_cities_list);
        citiesSearchField = findViewById(R.id.edt_city_search_field);
        cityListView = findViewById(R.id.recycler_city_list);
        addCityBtn = findViewById(R.id.add_city_btn);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
    }

    private void setAddCityBtnClickListener(final SQLiteDatabase database) {
        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new CityAddDialog(CitiesListActivity.this, database, cityListView);
                dialog.show();
            }
        });
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


