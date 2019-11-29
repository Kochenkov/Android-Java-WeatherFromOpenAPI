package com.vkochenkov.weatherfromopenapis;

class CitiesArray {

    public String[][] createCitiesArray() {

        String[][] citiesArray = new String[18][3];

        //инициализация массива с городами
        citiesArray[0][0] = "Санкт-Петербург";
        citiesArray[0][1] = "59.939095"; //latitude - широта
        citiesArray[0][2] = "30.315868"; //longitude - долгота

        citiesArray[1][0] = "Москва";
        citiesArray[1][1] = "55.755773";
        citiesArray[1][2] = "37.617761";

        citiesArray[2][0] = "Новосибирск";
        citiesArray[2][1] = "55.028739";
        citiesArray[2][2] = "82.906928";

        citiesArray[3][0] = "Екатеринбург";
        citiesArray[3][1] = "56.838002";
        citiesArray[3][2] = "60.597295";

        citiesArray[4][0] = "Нижний Новгород";
        citiesArray[4][1] = "56.323902";
        citiesArray[4][2] = "44.002267";

        citiesArray[5][0] = "Казань";
        citiesArray[5][1] = "55.795793";
        citiesArray[5][2] = "49.106585";

        citiesArray[6][0] = "Челябинск";
        citiesArray[6][1] = "55.159774";
        citiesArray[6][2] = "61.402455";

        citiesArray[7][0] = "Омск";
        citiesArray[7][1] = "54.989342";
        citiesArray[7][2] = "73.368212";

        citiesArray[8][0] = "Самара";
        citiesArray[8][1] = "53.195533";
        citiesArray[8][2] = "50.101801";

        citiesArray[9][0] = "Ростов-на-Дону";
        citiesArray[9][1] = "47.227151";
        citiesArray[9][2] = "39.744972";

        citiesArray[10][0] = "Уфа";
        citiesArray[10][1] = "54.734768";
        citiesArray[10][2] = "55.957838";

        citiesArray[11][0] = "Красноярск";
        citiesArray[11][1] = "56.008691";
        citiesArray[11][2] = "92.870529";

        citiesArray[12][0] = "Воронеж";
        citiesArray[12][1] = "51.661535";
        citiesArray[12][2] = "39.200287";

        citiesArray[13][0] = "Пермь";
        citiesArray[13][1] = "58.004785";
        citiesArray[13][2] = "56.237654";

        citiesArray[14][0] = "Волгоград";
        citiesArray[14][1] = "48.707103";
        citiesArray[14][2] = "44.516939";

        citiesArray[15][0] = "Хельсинки";
        citiesArray[15][1] = "60.169520";
        citiesArray[15][2] = "24.935450";

        citiesArray[16][0] = "Стокгольм";
        citiesArray[16][1] = "59.332580";
        citiesArray[16][2] = "18.064900";

        citiesArray[17][0] = "Осло";
        citiesArray[17][1] = "59.912730";
        citiesArray[17][2] = "10.746090";
        return citiesArray;
    }
}
