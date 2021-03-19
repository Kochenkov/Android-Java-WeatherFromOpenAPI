package com.vkochenkov.weatherfromopenapis.presentation.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vkochenkov.weatherfromopenapis.R;
import com.vkochenkov.weatherfromopenapis.data.db.DBHelper;
import com.vkochenkov.weatherfromopenapis.data.db.entities.City;
import com.vkochenkov.weatherfromopenapis.presentation.recycler.CityListAdapter;

public class CityDeleteDialog extends Dialog {

    private DBHelper dbHelper;
    private RecyclerView cityListView;
    private City city;

    public CityDeleteDialog(@NonNull Context context, DBHelper dbHelper, RecyclerView cityListView, City city) {
        super(context);
        this.dbHelper = dbHelper;
        this.cityListView = cityListView;
        this.city = city;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete_city);

        findViewById(R.id.apply_delete_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbHelper.deleteElement(dbHelper.getWritableDatabase(), city.getName());

                if (cityListView.getAdapter()!=null) {
                    CityListAdapter adapter = (CityListAdapter) cityListView.getAdapter();
                    adapter.updateDataListFromDb();
                    adapter.notifyDataSetChanged();
                }
                onBackPressed();
            }
        });

        findViewById(R.id.cancel_delete_city_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
