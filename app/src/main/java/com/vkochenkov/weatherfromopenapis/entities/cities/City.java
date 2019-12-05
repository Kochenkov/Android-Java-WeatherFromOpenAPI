package com.vkochenkov.weatherfromopenapis.entities.cities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class City {
    String name;
    String latitude;
    String longitude;

    public City(String name, String latitude, String longitude) {
        this.name = name;
        this.latitude = latitude;
        this. longitude = longitude;
    }
}
