package com.vkochenkov.weatherfromopenapis.WeatherApi;

import com.vkochenkov.weatherfromopenapis.Request.MainObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface WeatherApiInterface {

    @GET("{key}/{latitude},{longitude}")
    Call<MainObject> getWeather(
            @Path("key") String key,
            @Path("latitude") String latitude,
            @Path("longitude") String longitude
    );
}
