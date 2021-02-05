package com.vkochenkov.weatherfromopenapis.retrofit.responsefromweatherapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MainResponseObject {
    private String latitude;
    private String longitude;
    private String timezone;
    private DetailInformation currently;
    // то что ниже - есть в доке, но я не использую
//    private minutely;
//    private hourly;
//    private daily;
//    private flags;
    private String offset;
}
