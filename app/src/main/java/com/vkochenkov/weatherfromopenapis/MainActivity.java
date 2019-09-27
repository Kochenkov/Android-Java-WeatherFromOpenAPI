package com.vkochenkov.weatherfromopenapis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vkochenkov.weatherfromopenapis.entities.response.DetailInformation;
import com.vkochenkov.weatherfromopenapis.entities.response.MainResponseObject;
import com.vkochenkov.weatherfromopenapis.retrofit.WeatherApiManager;

import java.util.Date;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // вьюхи
    private TextView textViewResult;
    private EditText latitudeField;
    private EditText longitudeField;

    // параметры в урл
    private String KEY = "f5483d10bb2fca550ed960234826950f"; //ключ доступа аккаунта к АПИ
    private String LATITUDE = "1"; //широта
    private String LONGITUDE = "1"; //долгота
    private String UNITS = "si"; //параметр для получения данных в системе СИ

    // сообщение, отображаемое на экране
    private String message = "";

    MainResponseObject mainResponseObject;

    // локальные переменные для параметров
    private String localTime;
    private String temperatureCel;
    private String pressure;
    private Double pressurePa;
    private Double pressureMm;

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

                    mainResponseObject = response.body();

                    localTime = getLocalTime();
                    temperatureCel = mainResponseObject.getCurrently().getTemperature();
                    pressure = mainResponseObject.getCurrently().getPressure();
                    pressurePa = Double.parseDouble(pressure)*100; //переводим из строки и из гекто-Паскалей в Паскили
                    pressureMm = Math.ceil(pressurePa/133.3); //переводим в мм.рт.ст.

                    message =
                            "Широта: " + mainResponseObject.getLatitude() + "°" + "\n" +
                            "Долгота: " + mainResponseObject.getLongitude() + "°" + "\n" +
                            "Тайм-зона: " + mainResponseObject.getTimezone() + "\n" +
                            "Дата и время: " + localTime + "\n" +
                            "Температура: " + temperatureCel + " °C" + "\n" +
                            "Давление: " + pressurePa + "  Па" + "\n" +
                            "Давление: " + pressureMm + "  мм.рт.ст." + "\n" +
                            "Скорость ветра: " + response.body().getCurrently().getWindSpeed() + " м/с" + "\n" +
                            "Общий прогноз: " + response.body().getCurrently().getSummary()  + "\n" +
                            "ICON: " + response.body().getCurrently().getIcon()  + "\n";
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

    public void setLocation(View view) {
        latitudeField.setText("59.939095");
        longitudeField.setText("30.315868");
    }

    public void showMessage() {
        textViewResult.setText(message);
    }

    public String getLocalTime() {
        Date date = new Date();
        return date.toString();
    }
}