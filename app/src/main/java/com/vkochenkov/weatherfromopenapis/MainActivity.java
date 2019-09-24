package com.vkochenkov.weatherfromopenapis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.vkochenkov.weatherfromopenapis.Request.MainObject;
import com.vkochenkov.weatherfromopenapis.WeatherApi.WeatherApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;

    private String key = "f5483d10bb2fca550ed960234826950f";
    private String latitude = "56";
    private String longitude = "56";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
    }

    public void getWeather(View view) {
        callWeatherApi();
        //todo
    }

    public void callWeatherApi() {
        WeatherApiManager
            .getRequest()
            .getWeather(key,latitude,longitude)
            .enqueue(new Callback<MainObject>() {
                @Override
                public void onResponse(Call<MainObject> call, Response<MainObject> response) {
                    if (!response.isSuccessful()) {
                        textViewResult.setText("Code: " + response.code());
                        return;
                    }
                    MainObject mainObject = response.body();
                    textViewResult.append(mainObject.getLatitude());
                }

                @Override
                public void onFailure(Call<MainObject> call, Throwable t) {
                    textViewResult.setText(t.getMessage());
                }
            });
    }
}