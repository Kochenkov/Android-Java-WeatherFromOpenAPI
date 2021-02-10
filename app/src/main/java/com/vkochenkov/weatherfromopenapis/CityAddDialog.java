package com.vkochenkov.weatherfromopenapis;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.db.DBHelper;
import com.vkochenkov.weatherfromopenapis.recycler.CityListAdapter;

public class CityAddDialog extends Dialog {

    private DBHelper dbHelper;
    private RecyclerView cityListView;

    private EditText cityNameEdt;
    private EditText latitudeEdt;
    private EditText longitudeEdt;

    public CityAddDialog(@NonNull Context context, DBHelper dbHelper, RecyclerView cityListView) {
        super(context);
        this.cityListView = cityListView;
        this.dbHelper = dbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_city);

        initFields();

        findViewById(R.id.apply_add_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues contentValues = new ContentValues();

                contentValues.put(DBHelper.KEY_NAME, cityNameEdt.getText().toString());
                contentValues.put(DBHelper.KEY_LATITUDE, latitudeEdt.getText().toString());
                contentValues.put(DBHelper.KEY_LONGITUDE, longitudeEdt.getText().toString());

                dbHelper.getWritableDatabase().replace(DBHelper.CITIES_TABLE, DBHelper.DATABASE_NAME, contentValues);

                if (cityListView.getAdapter()!=null) {
                    CityListAdapter adapter = (CityListAdapter) cityListView.getAdapter();
                    adapter.updateDataList();
                    adapter.notifyDataSetChanged();
                }

                onBackPressed();
            }
        });

        findViewById(R.id.cancel_add_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void initFields() {
        cityNameEdt = findViewById(R.id.add_city_name_edt);
        latitudeEdt = findViewById(R.id.add_latitude_edt);
        longitudeEdt = findViewById(R.id.add_longitude_edt);
    }
}
