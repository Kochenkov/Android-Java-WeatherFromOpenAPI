package com.vkochenkov.weatherfromopenapis;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class CitiesListActivity extends ListActivity {

    private Toolbar toolbar;

    final String[] cityNamesArray = new String[] { "Санкт-Петербург", "Москва", "Новосибирск",
            "Екатеринбург", "Нижний Новгород", "Казань", "Челябинск", "Омск", "Самара",
            "Ростов-на-Дону", "Уфа", "Красноярск", "Воронеж", "Пермь", "Волгоград" };

    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                cityNamesArray);

        setListAdapter(mAdapter);

       // toolbar = findViewById(R.id.toolbar_cities_list);
       // setSupportActionBar(toolbar);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed(); // Implemented by activity
//            }
//        });
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onBackPressed();
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getApplicationContext(),
                "Вы выбрали " + (position + 1) + " элемент", Toast.LENGTH_SHORT).show();
    }

//    public void setCityLocation(View view) {
//        MainActivity.latitudeField.setText("59.939095");
//        MainActivity.longitudeField.setText("30.315868");
//    }
}
