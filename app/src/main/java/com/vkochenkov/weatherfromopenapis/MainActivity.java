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
import android.widget.ImageView;
import android.widget.TextView;

import com.vkochenkov.weatherfromopenapis.entities.response.MainResponseObject;
import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiManager;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    //вьюхи
    public static EditText latitudeField;
    public static EditText longitudeField;
    private TextView textViewResult;
    private ImageView iconViewImage;
    private Toolbar toolbar;

    //параметры, проставляемые в урл
    private String KEY = "f5483d10bb2fca550ed960234826950f"; //ключ доступа аккаунта к АПИ
    private String LATITUDE = "1"; //широта
    private String LONGITUDE = "1"; //долгота
    private String UNITS = "si"; //параметр для получения данных в системе СИ

    //сообщения, отображаемое на экране
    private String message = "";

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
    LocationManager locationManager;

    //флаг на таймер
    private Boolean locationListenerFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        longitudeField = findViewById(R.id.edt_longitude);
        latitudeField = findViewById(R.id.edt_latitude);
        iconViewImage = findViewById(R.id.img_view_icon);

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
                            alertTitle = "Что-то пошло не так";
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

                        showMessage(message);
                        showIcon(icon);

                        //todo фича: добавить погоду на завтра
                    }

                    @Override
                    public void onFailure(Call<MainResponseObject> call, Throwable t) {
                        alertTitle = "Что-то пошло не так";
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

    public void showMessage(String message) {
        textViewResult.setText(message);
        textViewResult.setVisibility(textViewResult.VISIBLE);
    }

    public void showIcon(String icon) {
        switch (icon) {
            case ("clear-day"):
                iconViewImage.setImageResource(R.drawable.clear_day);
                break;
            case ("clear-night"):
                iconViewImage.setImageResource(R.drawable.clear_night);
                break;
            case ("rain"):
                iconViewImage.setImageResource(R.drawable.rain);
                break;
            case ("snow"):
                iconViewImage.setImageResource(R.drawable.snow);
                break;
            case ("sleet"):
                iconViewImage.setImageResource(R.drawable.sleet);
                break;
            case ("wind"):
                iconViewImage.setImageResource(R.drawable.wind);
                break;
            case ("fog"):
                iconViewImage.setImageResource(R.drawable.fog);
                break;
            case ("cloudy"):
                iconViewImage.setImageResource(R.drawable.cloudy);
                break;
            case ("partly-cloudy-day"):
                iconViewImage.setImageResource(R.drawable.partly_cloudy_day);
                break;
            case ("partly-cloudy-night"):
                iconViewImage.setImageResource(R.drawable.partly_cloudy_night);
                break;
            default:
                //todo добавить дефолтную иконку, если пришло что-то иное
                break;
        }
        iconViewImage.setVisibility(iconViewImage.VISIBLE);
    }


    //запрос на получение локации
    public void setDeviceLocation(View view) {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            alertTitle = "Системное сообщение";
            alertMessage = "Пожалуйста, зайдите в настройки телефона, и включите разрешение геолокации для этого приложения.";
            alertButtonText = "Понятно";
            alertIcon = R.drawable.flag;
            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
            return;
        }

        //запускаем получение геолокации
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, locationListener);

        //выставляем флаг и запускаем таймер, что бы геолокация не жрала батарею вечно
        locationListenerFlag = true;
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (locationListenerFlag) {
                    locationManager.removeUpdates(locationListener);

                    alertTitle = "Системное сообщение";
                    alertMessage = "Сейчас установить вашу локацию невозможно. Попробуйте, пожалуйста, позже";
                    alertButtonText = "Понятно";
                    alertIcon = R.drawable.flag;
                    showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
                    //todo код выше не работает по непонятной причине. Возможно по таймеру нельзя вызвать алерт
                }
            }
        }, 15000);
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
            } else {
                //попадаем сюда, когда приходит невалидный ответ
                alertTitle = "Что-то пошло не так";
                alertMessage = "Некорректный ответ от сервиса. Попробуйте пожалуйста позже.";
                alertButtonText = "Понятно";
                alertIcon = R.drawable.eclipse;
                showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
            }
            //убираем флаг таймера
            locationListenerFlag = false;
            //останавливаем работу requestLocationUpdates
            locationManager.removeUpdates(locationListener);
        }

        //хз для чего нужны эти методы, но без них лисенера не будет
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    public void showProgramInfo(View view) {
        alertTitle = "О программе";
        alertMessage = "Программа позволяет получить погоду в заданном регионе по координатам широты и долготы. " +
                "Координаты можно ввести одним из трёх способов: \n" +
                " - выбрать город из списка, \n" +
                " - получить текущие координаты местонахождения девайса по gps, \n" +
                " - прописать координаты вручную.";
        alertButtonText = "Понятно";
        alertIcon = R.drawable.clear_day;
        showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);
    }

    //показать алерт
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

    //переопределение метода без иконки
    public void showAlert(String alertTitle, String alertMessage, String alertButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(alertTitle)
                .setMessage(alertMessage)
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

    public void cleanFields(View view) {
        latitudeField.setText("");
        longitudeField.setText("");
    }

    public void openCitiesListActivity(View view) {
        //todo добавить открытие экрана
        Intent intent = new Intent(MainActivity.this, CitiesListActivity.class);
        startActivity(intent);
    }
}