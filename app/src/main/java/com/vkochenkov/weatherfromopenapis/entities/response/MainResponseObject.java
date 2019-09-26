package com.vkochenkov.weatherfromopenapis.entities.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MainResponseObject {

    private String latitude;
    private String longitude;
    private String timezone;
    private DetailInformation currently;
//    private minutely;
//    private hourly;
//    private daily;
//    private flags;
    private String offset;
}
