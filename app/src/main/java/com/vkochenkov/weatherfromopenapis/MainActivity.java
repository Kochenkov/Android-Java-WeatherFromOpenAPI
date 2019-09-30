package com.vkochenkov.weatherfromopenapis;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
    private ImageView iconViewImage;

    // параметры, проставляемые в урл
    private String KEY = "f5483d10bb2fca550ed960234826950f"; //ключ доступа аккаунта к АПИ
    private String LATITUDE = "1"; //широта
    private String LONGITUDE = "1"; //долгота
    private String UNITS = "si"; //параметр для получения данных в системе СИ

    // сообщение, отображаемое на экране
    private String message = "";

    // главный объект, куда подставятся данные ответа от апишки
    MainResponseObject mainResponseObject;

    // локальные переменные для параметров
    private String localTime;
    private String temperatureCel;
    private String pressure;
    private String humidity;
    private String icon;
    private Double humidityPercent;
    private Double pressurePa;
    private Double pressureMm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);
        longitudeField = findViewById(R.id.edt_longitude);
        latitudeField = findViewById(R.id.edt_latitude);
        iconViewImage = findViewById(R.id.img_view_icon);
    }

    public void getParamsFromFields() {
        LATITUDE = latitudeField.getText().toString();
        LONGITUDE = longitudeField.getText().toString();
        //todo add parser for change "," to "."
    }

    // основной метод отправки запроса и приема ответа
    public void getWeatherFromApi() {
        WeatherApiManager
            .getRequest()
            .getWeather(KEY, LATITUDE, LONGITUDE, UNITS)
            .enqueue(new Callback<MainResponseObject>() {
                @Override
                public void onResponse(Call<MainResponseObject> call, Response<MainResponseObject> response) {
                    if (!response.isSuccessful()) {
                        message = "Something went wrong. Response code: " + response.code();
                        showMessage(message);
                        return;
                    }

                    mainResponseObject = response.body();

                    //localTime = getLocalTime(); - не будет использоваться
                    temperatureCel = mainResponseObject.getCurrently().getTemperature();
                    humidity = mainResponseObject.getCurrently().getHumidity();
                    humidityPercent = Double.parseDouble(humidity)*100;
                    pressure = mainResponseObject.getCurrently().getPressure();
                    pressurePa = Double.parseDouble(pressure)*100; //переводим из строки и из гекто-Паскалей в Паскили
                    pressureMm = Math.ceil(pressurePa/133.3); //переводим в мм.рт.ст.
                    icon = response.body().getCurrently().getIcon(); //для теста

                    message =
                            "Текущая погода в заданной области" + "\n" +
                            "Широта: " + mainResponseObject.getLatitude() + "°" + "\n" +
                            "Долгота: " + mainResponseObject.getLongitude() + "°" + "\n" +
                            "Тайм-зона: " + mainResponseObject.getTimezone() + "\n" +
                            "Температура: " + temperatureCel + "°C" + "\n" +
                            "Влажность: " + humidityPercent + "%" + "\n" +
                            "Давление: " + pressureMm + "  мм.рт.ст." + "\n" +
                            "Скорость ветра: " + response.body().getCurrently().getWindSpeed() + " м/с" + "\n" +
                            "Общий прогноз: " + response.body().getCurrently().getSummary()  + "\n";
                    showMessage(message);

                    showIcon(icon);

                    //todo добавить запрос местоположения девайса

                    //todo добавить погоду на завтра
                }

                @Override
                public void onFailure(Call<MainResponseObject> call, Throwable t) {
                    message = t.getMessage();
                    showMessage(message);
                }
            });
    }

    // нажатие на кнопку
    public void getWeather(View view) throws InterruptedException {
        getParamsFromFields();
        getWeatherFromApi();
    }

    // нажатие на кнопку
    public void setSPbLocation(View view) {
        latitudeField.setText("59.939095");
        longitudeField.setText("30.315868");
    }

    public void showMessage(String message) {
        textViewResult.setText(message);
    }

//    public String getLocalTime() {
//        Date date = new Date();
//        return date.toString();
//    }

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
                //todo
                break;
        }
    }

    public void setLocation(View view) {

    }
}