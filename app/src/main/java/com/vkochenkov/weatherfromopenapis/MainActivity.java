package com.vkochenkov.weatherfromopenapis;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vkochenkov.weatherfromopenapis.entities.response.MainResponseObject;
import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private TextView textViewResult;
    private EditText latitudeField;
    private EditText longitudeField;

    private String KEY = "f5483d10bb2fca550ed960234826950f"; //ключ доступа аккаунта к АПИ
    private String LATITUDE = "1"; //широта
    private String LONGITUDE = "1"; //долгота
    private String UNITS = "si"; // параметр для получения данных в системе СИ

    private String message = "init";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        longitudeField = findViewById(R.id.edt_longitude);
        latitudeField = findViewById(R.id.edt_latitude);
    }

    public void getParamsFromFields() {
        LATITUDE = latitudeField.getText().toString();
        LONGITUDE = longitudeField.getText().toString();
        //todo add parser for change "," to "."
    }

    public void getWeatherFromApi() {
        WeatherApiManager
            .getRequest()
            .getWeather(KEY, LATITUDE, LONGITUDE, UNITS)
            .enqueue(new Callback<MainResponseObject>() {
                @Override
                public void onResponse(Call<MainResponseObject> call, Response<MainResponseObject> response) {
                    if (!response.isSuccessful()) {
                        message = "Something went wrong. Response code: " + response.code();
                        showMessage();
                        return;
                    }
                    message =
                            "широта: " + response.body().getLatitude() + "\n" +
                            "долгота: " + response.body().getLongitude() + "\n" +
                            "тайм-зона: " + response.body().getTimezone() + "\n" +
                            "время: " + response.body().getCurrently().getTime() + "\n" +
                            "температура: " + response.body().getCurrently().getTemperature() + " °C" + "\n" +
                            "давление: " + response.body().getCurrently().getPressure() + "\n" +
                            "скорость ветра: " + response.body().getCurrently().getWindSpeed() + "\n";
                    // todo нужно разобраться с time
                    // todo добавить размерности
                    showMessage();
                }

                @Override
                public void onFailure(Call<MainResponseObject> call, Throwable t) {
                    message = t.getMessage();
                    showMessage();
                }
            });
    }

    public void getWeather(View view) throws InterruptedException {
        getParamsFromFields();
        getWeatherFromApi();
    }

    @SuppressLint("SetTextI18n")
    public void setLocation(View view) {
        latitudeField.setText("59.939095");
        longitudeField.setText("30.315868");
    }

    public void showMessage() {
        textViewResult.setText(message);
    }
}