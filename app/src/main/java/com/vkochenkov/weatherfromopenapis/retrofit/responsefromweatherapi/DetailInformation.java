package com.vkochenkov.weatherfromopenapis.retrofit.responsefromweatherapi;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DetailInformation {
    private String time;
    private String summary;
    private String icon;
    private String nearestStormDistance;
    private String nearestStormBearing;
    private String precipIntensity;
    private String precipProbability;
    private String temperature;
    private String apparentTemperature;
    private String dewPoint;
    private String humidity;
    private String pressure;
    private String windSpeed;
    private String windGust;
    private String windBearing;
    private String cloudCover;
    private String uvIndex;
    private String visibility;
    private String ozone;
}
