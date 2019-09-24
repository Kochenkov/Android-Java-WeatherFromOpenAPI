package com.vkochenkov.weatherfromopenapis.Request;

import lombok.Getter;

@Getter
public class MainObject {

    private String latitude;
    private String longitude;
    private String timezone;
    private WeatherInformation currently;
    private Timely hourly;
    private Timely daily;
    private String offset;
}
