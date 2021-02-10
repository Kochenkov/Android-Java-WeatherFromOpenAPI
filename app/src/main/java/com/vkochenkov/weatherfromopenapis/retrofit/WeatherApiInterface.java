package com.vkochenkov.weatherfromopenapis.retrofit;

import com.vkochenkov.weatherfromopenapis.retrofit.responsefromweatherapi.MainResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WeatherApiInterface {

    @GET("{latitude},{longitude}")
    Call<MainResponseObject> getWeather(
            @Path("latitude") String latitude,
            @Path("longitude") String longitude,
            @Query("units") String units
    );
}
