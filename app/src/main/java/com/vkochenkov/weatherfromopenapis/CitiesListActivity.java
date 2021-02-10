package com.vkochenkov.weatherfromopenapis;

import android.app.Dialog;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
        setTextWatcherForSearchCity();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void initRecycler(final SQLiteDatabase database) {
        cityListView.setLayoutManager(new LinearLayoutManager(this));
        CityClickListener cityClickListener = new CityClickListener() {
            @Override
            public View.OnClickListener onCityClickListener(final City city) {
                return new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo нужно переписать на старт активити фо резалт или бандл?
                        MainActivity.latitudeField.setText(city.getLatitude());
                        MainActivity.longitudeField.setText(city.getLongitude());
                        onBackPressed();
                    }
                };
            }

            @Override
            public View.OnLongClickListener onCityLongClickListener(int position, final City city) {

                return new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        Dialog dialog = new CityDeleteDialog(CitiesListActivity.this, dbHelper, cityListView, city);
                        dialog.show();

                        return true;
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
                Dialog dialog = new CityAddDialog(CitiesListActivity.this, dbHelper, cityListView);
                dialog.show();
            }
        });
    }

    private void setTextWatcherForSearchCity() {
        citiesSearchField.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                CityListAdapter adapter = (CityListAdapter) cityListView.getAdapter();
                if (adapter!=null) {
                    adapter.findAndShowCityItems(charSequence.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }
}
