package com.vkochenkov.weatherfromopenapis;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vkochenkov.weatherfromopenapis.entities.responsefromweatherapi.MainResponseObject;
import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //вьюхи
    public static EditText latitudeField;
    public static EditText longitudeField;
    private Toolbar toolbar;

    //параметры, проставляемые в урл
    private String KEY = "f5483d10bb2fca550ed960234826950f"; //ключ доступа аккаунта к АПИ
    private String LATITUDE = "1"; //широта
    private String LONGITUDE = "1"; //долгота
    private String UNITS = "si"; //параметр для получения данных в системе СИ

    //сообщения, отображаемое на экране
    private String message = "";

    //прочие сущности
    private String alertMessage;
    private String alertButtonText;
    private String alertTitle;
    private int alertIcon;

    //главный объект, куда подставятся данные ответа от апишки
    MainResponseObject mainResponseObject;

    //локальные переменные для параметров
    private String temperatureCel;
    private String pressure;
    private String humidity;
    private String icon;
    private Double humidityPercent;
    private Double pressurePa;
    private Double pressureMm;

    //менеджер геолокации
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        longitudeField = findViewById(R.id.edt_longitude);
        latitudeField = findViewById(R.id.edt_latitude);

        toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
    }

    public void getParamsFromFields() {
        LATITUDE = latitudeField.getText().toString();
        LONGITUDE = longitudeField.getText().toString();
    }

    //основной метод отправки запроса и приема ответа
    public void getWeatherFromApi() {
        WeatherApiManager
                .getRequest()
                .getWeather(KEY, LATITUDE, LONGITUDE, UNITS)
                .enqueue(new Callback<MainResponseObject>() {
                    @Override
                    public void onResponse(Call<MainResponseObject> call, Response<MainResponseObject> response) {
                        if (!response.isSuccessful()) {
                            alertTitle = "Похоже, что-то пошло не так";
                            alertMessage = "Код ответа от сервера: " + response.code();
                            alertButtonText = "Понятно";
                            alertIcon = R.drawable.eclipse;
                            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
                            return;
                        }

                        mainResponseObject = response.body();

                        temperatureCel = mainResponseObject.getCurrently().getTemperature();
                        humidity = mainResponseObject.getCurrently().getHumidity();
                        humidityPercent = Double.parseDouble(humidity) * 100;
                        pressure = mainResponseObject.getCurrently().getPressure();
                        pressurePa = Double.parseDouble(pressure) * 100; //переводим из строки и из гекто-Паскалей в Паскили
                        pressureMm = Math.ceil(pressurePa / 133.3); //переводим в мм.рт.ст.
                        icon = response.body().getCurrently().getIcon(); //для теста

                        message =
                                "Заданная область:" + "\n" +
                                        "широта: " + mainResponseObject.getLatitude() + "°;" + "\n" +
                                        "долгота: " + mainResponseObject.getLongitude() + "°;" + "\n" +
                                        "тайм-зона: " + mainResponseObject.getTimezone() + "." + "\n" +
                                        "\n" +
                                        "Текущая погода в заданной области:" + "\n" +
                                        "температура: " + temperatureCel + "°C;" + "\n" +
                                        "влажность: " + humidityPercent + "%;" + "\n" +
                                        "давление: " + pressureMm + "  мм.рт.ст.;" + "\n" +
                                        "скорость ветра: " + response.body().getCurrently().getWindSpeed() + " м/с;" + "\n" +
                                        "общий прогноз: " + response.body().getCurrently().getSummary() + "." + "\n";

                        alertTitle = "Такие дела";
                        alertMessage = message;
                        alertButtonText = "Понятно";
                        alertIcon = setIcon(icon);
                        showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
                    }

                    @Override
                    public void onFailure(Call<MainResponseObject> call, Throwable t) {
                        alertTitle = "Похоже, что-то пошло не так";
                        alertMessage = t.getMessage();
                        alertButtonText = "Понятно";
                        alertIcon = R.drawable.eclipse;
                        showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
                    }
                });
    }

    public void getWeather(View view) {
        getParamsFromFields();
        getWeatherFromApi();
    }

    public int setIcon(String icon) {
        switch (icon) {
            case ("clear-day"):
                return R.drawable.clear_day;
            case ("clear-night"):
                return R.drawable.clear_night;
            case ("rain"):
                return R.drawable.rain;
            case ("snow"):
                return R.drawable.snow;
            case ("sleet"):
                return R.drawable.sleet;
            case ("wind"):
                return R.drawable.wind;
            case ("fog"):
                return R.drawable.fog;
            case ("cloudy"):
                return R.drawable.cloudy;
            case ("partly-cloudy-day"):
                return R.drawable.partly_cloudy_day;
            case ("partly-cloudy-night"):
                return R.drawable.partly_cloudy_night;
            default:
                return R.drawable.eclipse;
        }
    }

    //запрос на получение локации
    public void setDeviceLocation(View view) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //запускаем получение геолокации
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            alertTitle = "Системное сообщение";
            alertMessage = "Пожалуйста, зайдите в настройки телефона, и включите разрешение геолокации для этого приложения.";
            alertButtonText = "Понятно";
            alertIcon = R.drawable.flag;
            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
    }

    //лисенер геолокации
    private LocationListener locationListener = new LocationListener() {
        //получили ответ о локации
        @Override
        public void onLocationChanged(Location location) {
            if (location!=null) {
                //попадаем сюда, когда получаем ответ о местоположении
                latitudeField.setText(String.valueOf(location.getLatitude()));
                longitudeField.setText(String.valueOf(location.getLongitude()));
                Toast.makeText(getApplicationContext(), "Координаты девайса получены", Toast.LENGTH_SHORT).show();
            } else {
                //попадаем сюда, когда приходит невалидный ответ
                alertTitle = "Что-то пошло не так";
                alertMessage = "Некорректный ответ от сервиса. Попробуйте пожалуйста позже.";
                alertButtonText = "Понятно";
                alertIcon = R.drawable.eclipse;
                showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
            }
            //останавливаем работу requestLocationUpdates
            locationManager.removeUpdates(locationListener);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onProviderDisabled(String provider) {}
    };

    public void showProgramInfo(View view) {
        alertTitle = "О программе";
        alertMessage = "Программа позволяет получить текущую погоду для любого места, нужно только указать координаты широты и долготы. " +
                "Это можно сделать одним из трёх способов: \n" +
                " - выбрать город из списка; \n" +
                " - получить текущие координаты телефона; \n" +
                " - прописать координаты вручную.";
        alertButtonText = "Понятно";
        alertIcon = R.drawable.clear_day;
        showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
    }

    public void showAlert(String alertTitle, String alertMessage, String alertButtonText, int alertIcon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(alertTitle)
                .setMessage(alertMessage)
                .setIcon(alertIcon)
                .setCancelable(false)
                .setNegativeButton(alertButtonText,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

//    //перегрузка метода без иконки
//    public void showAlert(String alertTitle, String alertMessage, String alertButtonText) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//        builder.setTitle(alertTitle)
//                .setMessage(alertMessage)
//                .setCancelable(false)
//                .setNegativeButton(alertButtonText,
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//        AlertDialog alert = builder.create();
//        alert.show();
//    }

    public void cleanFields(View view) {
        latitudeField.setText("");
        longitudeField.setText("");
    }

    public void openCitiesListActivity(View view) {
        Intent intent = new Intent(MainActivity.this, CitiesListActivity.class);
        startActivity(intent);
    }
}