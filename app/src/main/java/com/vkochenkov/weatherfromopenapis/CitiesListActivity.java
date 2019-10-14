package com.vkochenkov.weatherfromopenapis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class CitiesListActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cities_list);

        toolbar = findViewById(R.id.toolbar_cities_list);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Implemented by activity
            }
        });
    }

    public void setCityLocation(View view) {
        MainActivity.latitudeField.setText("59.939095");
        MainActivity.longitudeField.setText("30.315868");
        super.onBackPressed();
        //test
    }
}
