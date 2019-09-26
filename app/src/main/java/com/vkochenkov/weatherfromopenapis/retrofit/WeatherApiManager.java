package com.vkochenkov.weatherfromopenapis.retrofit;

import android.app.Application;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherApiManager extends Application {

    private Retrofit retrofit;
    private static WeatherApiInterface weatherApiInterface;
    private final String BASE_URL = "https://api.darksky.net/forecast/";

    @Override
    public void onCreate() {
        super.onCreate();

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApiInterface = retrofit.create(WeatherApiInterface.class);
    }

    public static WeatherApiInterface getRequest() {
        return weatherApiInterface;
    }
}
