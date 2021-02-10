package com.vkochenkov.weatherfromopenapis;

import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiInterface;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class App extends android.app.Application {

    private static WeatherApiInterface weatherApiInterface;
    private static final String BASE_URL = "https://api.darksky.net/forecast/";
    private static final String KEY = "f5483d10bb2fca550ed960234826950f"; //ключ доступа аккаунта к АПИ

    @Override
    public void onCreate() {
        super.onCreate();
        //инициализируем ретрофит в классе приложения
        initRetrofit();
    }

    public static WeatherApiInterface getRequest() {
        return weatherApiInterface;
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL + KEY + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherApiInterface = retrofit.create(WeatherApiInterface.class);
    }
}
