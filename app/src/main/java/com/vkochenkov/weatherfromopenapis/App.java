package com.vkochenkov.weatherfromopenapis;

import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends android.app.Application {

    //инициализируем ретрофит в классе приложения
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
