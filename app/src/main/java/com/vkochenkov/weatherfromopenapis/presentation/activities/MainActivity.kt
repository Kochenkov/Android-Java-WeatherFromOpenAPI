package com.vkochenkov.weatherfromopenapis.presentation.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.presentation.MainScreenViewModel

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val viewModel: MainScreenViewModel by lazy {
        ViewModelProvider(this).get(MainScreenViewModel::class.java)
    }

    //широта
    private var latitude = "1"
    //долгота
    private var longitude = "1"

    //вьюхи
    private lateinit var latitudeField: EditText
    private lateinit var longitudeField: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: ProgressBar
    private lateinit var btnSelectCity: Button
    private lateinit var btnGetCoorginates: Button
    private lateinit var btnClearFields: Button
    private lateinit var btnGetWeather: Button
    private lateinit var btnInfo: ImageButton

    //прочие сущности
    private var alertMessage: String? = null
    private var alertButtonText: String? = null
    private var alertTitle: String? = null
    private var alertIcon = 0


    //менеджер геолокации
    private var locationManager: LocationManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initFields()
        setSupportActionBar(toolbar)
        setOnClickListeners()
        initObserveForViewModel()
        setTextWatcherForCoordinatesFields()
    }

    private fun initObserveForViewModel() {
        viewModel.weatherLiveData.observe(this, Observer {
            var temperatureCel = it.getCurrently().getTemperature();
            var humidity = it.getCurrently().getHumidity();
            var humidityPercent = humidity.toDouble() * 100;
            var pressure = it.getCurrently().getPressure();
            var pressurePa =
                pressure.toDouble() * 100 //переводим из строки и из гекто-Паскалей в Паскили
            var pressureMm = Math.ceil(pressurePa / 133.3); //переводим в мм.рт.ст.
            var icon = it.getCurrently().getIcon(); //для теста

            var message =
                "Заданная область:" + "\n" +
                        "широта: " + it.getLatitude() + "°;" + "\n" +
                        "долгота: " + it.getLongitude() + "°;" + "\n" +
                        "тайм-зона: " + it.getTimezone() + "." + "\n" +
                        "\n" +
                        "Текущая погода в заданной области:" + "\n" +
                        "температура: " + temperatureCel + "°C;" + "\n" +
                        "влажность: " + humidityPercent + "%;" + "\n" +
                        "давление: " + pressureMm + "  мм.рт.ст.;" + "\n" +
                        "скорость ветра: " + it.getCurrently().getWindSpeed() + " м/с;" + "\n" +
                        "общий прогноз: " + it.getCurrently().getSummary() + "." + "\n";

            alertTitle = "Такие дела";
            alertMessage = message;
            alertButtonText = "Понятно";
            alertIcon = setIcon(icon);
            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon)

            progressBar.visibility = View.GONE
        })

        viewModel.errorLiveData.observe(this, Observer {
            alertTitle = "Похоже, что-то пошло не так";
            alertMessage = it
            alertButtonText = "Понятно";
            alertIcon = R.drawable.eclipse;
            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon);

            progressBar.visibility = View.GONE
        })
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            latitudeField.setText(data.extras!!.getString("latitude"))
            longitudeField.setText(data.extras!!.getString("longitude"))
        }
    }

    val paramsFromFields: Unit
        get() {
            latitude = latitudeField.text.toString()
            longitude = longitudeField.text.toString()
        }

    private fun getWeather() {
        paramsFromFields
        progressBar.visibility = View.VISIBLE
        viewModel.getWeatherFromApi(latitude, longitude)

        //validationFieldsAndGetWeatherFromApi();
    }

    private fun setIcon(icon: String?): Int {
        return when (icon) {
            "clear-day" -> R.drawable.clear_day
            "clear-night" -> R.drawable.clear_night
            "rain" -> R.drawable.rain
            "snow" -> R.drawable.snow
            "sleet" -> R.drawable.sleet
            "wind" -> R.drawable.wind
            "fog" -> R.drawable.fog
            "cloudy" -> R.drawable.cloudy
            "partly-cloudy-day" -> R.drawable.partly_cloudy_day
            "partly-cloudy-night" -> R.drawable.partly_cloudy_night
            else -> R.drawable.eclipse
        }
    }

    //запрос на получение локации
    private fun getDeviceLocation() {
        locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager

        //запускаем получение геолокации
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            alertTitle = "Системное сообщение"
            alertMessage =
                "Пожалуйста, зайдите в настройки телефона, и включите разрешение геолокации для этого приложения."
            alertButtonText = "Понятно"
            alertIcon = R.drawable.flag
            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon)
            return
        }
        locationManager!!.requestLocationUpdates(
            LocationManager.NETWORK_PROVIDER,
            1000,
            0f,
            locationListener
        )
    }

    //лисенер геолокации
    private val locationListener: LocationListener = object : LocationListener {
        //получили ответ о локации
        override fun onLocationChanged(location: Location) {
            if (location != null) {
                //попадаем сюда, когда получаем ответ о местоположении
                latitudeField.setText(location.latitude.toString())
                longitudeField.setText(location.longitude.toString())
                Toast.makeText(
                    applicationContext,
                    "Координаты девайса получены",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                //попадаем сюда, когда приходит невалидный ответ
                alertTitle = "Что-то пошло не так"
                alertMessage = "Некорректный ответ от сервиса. Попробуйте пожалуйста позже."
                alertButtonText = "Понятно"
                alertIcon = R.drawable.eclipse
                showAlert(alertTitle, alertMessage, alertButtonText, alertIcon)
            }
            //останавливаем работу requestLocationUpdates
            locationManager!!.removeUpdates(this)
        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderEnabled(provider: String) {}
        override fun onProviderDisabled(provider: String) {}
    }

    private fun showProgramInfo() {
        alertTitle = "О программе"
        alertMessage =
            """Программа позволяет получить текущую погоду для любого места, нужно только указать координаты широты и долготы. Это можно сделать одним из трёх способов: 
 - выбрать город из списка; 
 - получить текущие координаты телефона; 
 - прописать координаты вручную."""
        alertButtonText = "Понятно"
        alertIcon = R.drawable.clear_day
        showAlert(alertTitle, alertMessage, alertButtonText, alertIcon)
    }

    private fun showAlert(
        alertTitle: String?,
        alertMessage: String?,
        alertButtonText: String?,
        alertIcon: Int
    ) {
        val builder =
            AlertDialog.Builder(this@MainActivity)
        builder.setTitle(alertTitle)
            .setMessage(alertMessage)
            .setIcon(alertIcon)
            .setCancelable(false)
            .setNegativeButton(
                alertButtonText
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun cleanFields() {
        latitudeField.setText("")
        longitudeField.setText("")
    }

    private fun openCitiesListActivity() {
        val intent = Intent(this@MainActivity, CitiesListActivity::class.java)
        startActivityForResult(intent, 100)
    }

    private fun initFields() {
        longitudeField = findViewById(R.id.edt_longitude)
        latitudeField = findViewById(R.id.edt_latitude)
        progressBar = findViewById(R.id.progress_bar)
        toolbar = findViewById(R.id.toolbar_main)
        btnSelectCity = findViewById(R.id.btn_select_city)
        btnGetCoorginates = findViewById(R.id.btn_get_location)
        btnClearFields = findViewById(R.id.btn_clean_fields)
        btnGetWeather = findViewById(R.id.btn_get_weather)
        btnInfo = findViewById(R.id.info_image_btn)
    }

    private fun setOnClickListeners() {
        btnSelectCity.setOnClickListener(this)
        btnGetCoorginates.setOnClickListener(this)
        btnClearFields.setOnClickListener(this)
        btnGetWeather.setOnClickListener(this)
        btnInfo.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_select_city -> openCitiesListActivity()
            R.id.btn_get_location -> getDeviceLocation()
            R.id.btn_clean_fields -> cleanFields()
            R.id.btn_get_weather -> getWeather()
            R.id.info_image_btn -> showProgramInfo()
        }
    }

    private fun setTextWatcherForCoordinatesFields() {
        val textWatcher = object : TextWatcher {
            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                //todo нужно сделать обработку для двух полей, что бы они зависили друг от друга
                if (charSequence.isNotEmpty()){
                    btnClearFields.visibility = View.VISIBLE
                    btnGetWeather.visibility = View.VISIBLE
                } else {
                    btnClearFields.visibility = View.GONE
                    btnGetWeather.visibility = View.GONE
                }
            }

            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun afterTextChanged(editable: Editable) {}
        }

        latitudeField.addTextChangedListener(textWatcher)
        longitudeField.addTextChangedListener(textWatcher)
    }
}