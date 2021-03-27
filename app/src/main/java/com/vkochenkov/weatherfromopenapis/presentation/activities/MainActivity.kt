package com.vkochenkov.weatherfromopenapis.presentation.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.presentation.viewmodel.MainScreenViewModel
import kotlin.math.roundToInt


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
    private lateinit var btnClearLatitude: ImageButton
    private lateinit var btnClearLongitude: ImageButton
    private lateinit var btnGetWeather: Button
    private lateinit var btnInfo: ImageButton

    //прочие сущности
    private var alertMessage: String = ""
    private var alertButtonText: String = ""
    private var alertTitle: String = ""
    private var alertIcon = 0

    //менеджер геолокации
    private lateinit var locationManager: LocationManager
    private lateinit var inputMethodManager: InputMethodManager


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
            var temperatureCel = it.getCurrently().getTemperature()
            var humidity = it.getCurrently().getHumidity()
            var humidityPercent = humidity.toDouble() * 100
            var pressure = it.getCurrently().getPressure()
            var pressurePa =
                pressure.toDouble() * 100 //переводим из строки и из гекто-Паскалей в Паскили
            var pressureMm = Math.ceil(pressurePa / 133.3) //переводим в мм.рт.ст.
            var icon = it.getCurrently().getIcon()

            var message =
                "Широта: " + round(it.getLatitude().toFloat()) + "°;" + "\n" +
                        "Долгота: " + round(it.getLongitude().toFloat()) + "°;" + "\n" +
                        "Тайм-зона: " + it.getTimezone() + "." + "\n" +
                        "\n" +
                        "Температура: " + round(temperatureCel.toFloat()) + "°C;" + "\n" +
                        "Влажность: " + round(humidityPercent.toFloat()) + "%;" + "\n" +
                        "Давление: " + round(pressureMm.toFloat()) + " мм.рт.ст.;" + "\n" +
                        "Скорость ветра: " + round(
                    it.getCurrently().getWindSpeed().toFloat()
                ) + " м/с;" +
                        "\n" +
                        "Общий прогноз: " + it.getCurrently().getSummary() + "." + "\n";

            alertTitle = "Прогноз погоды";
            alertMessage = message;
            alertButtonText = "Понятно";
            alertIcon = setIcon(icon);
            showAlert(alertTitle, alertMessage, alertButtonText, alertIcon)
            progressBar.visibility = View.INVISIBLE
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

    private fun getWeather() {
        latitude = latitudeField.text.toString()
        longitude = longitudeField.text.toString()
        validateFieldsAndGetWeatherFromApi()
    }

    private fun validateFieldsAndGetWeatherFromApi() {
        if (latitude == "" || longitude == "") {
            Toast.makeText(
                applicationContext,
                "Заполните поля с координатами!",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            progressBar.visibility = View.VISIBLE
            viewModel.getWeatherFromApi(latitude, longitude)
        }
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

    private fun getDeviceLocation() {
        //проверяем пермишен
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            val alertBuilder = AlertDialog.Builder(this@MainActivity)
            alertBuilder.setTitle("Системное сообщение")
                .setMessage("Пожалуйста, зайдите в настройки телефона, и включите разрешение геолокации для этого приложения.")
                .setIcon(R.drawable.flag)
                .setCancelable(true)
                .setNegativeButton("Понятно") { dialog, id -> dialog.cancel() }
                .setPositiveButton("Настройки") { dialog, it ->
                    val intent =
                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val uri =
                        Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                    dialog.cancel()
                }
            val alert = alertBuilder.create()
            alert.show()

        } else {
            progressBar.visibility = View.VISIBLE
            //устанавливаем два requestLocationUpdates
            locationManager!!.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                1000,
                10f,
                locationListener
            )
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000,
                10f,
                locationListener
            )
        }
    }

    //лисенер геолокации
    private val locationListener: LocationListener = object : LocationListener {
        //получили ответ о локации
        override fun onLocationChanged(location: Location) {
            //попадаем сюда, когда получаем ответ о местоположении
            latitudeField.setText(location.latitude.toString())
            longitudeField.setText(location.longitude.toString())
            Toast.makeText(
                applicationContext,
                "Координаты девайса получены",
                Toast.LENGTH_SHORT
            ).show()
            progressBar.visibility = View.INVISIBLE
            //останавливаем работу requestLocationUpdates
            locationManager.removeUpdates(this)
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
            .setCancelable(true)
            .setNegativeButton(
                alertButtonText
            ) { dialog, id -> dialog.cancel() }
        val alert = builder.create()
        alert.show()
    }

    private fun openCitiesListActivity() {
        val intent = Intent(this@MainActivity, CitiesListActivity::class.java)
        startActivityForResult(intent, 100)
    }

    private fun initFields() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

        longitudeField = findViewById(R.id.edt_longitude)
        latitudeField = findViewById(R.id.edt_latitude)
        progressBar = findViewById(R.id.progress_bar)
        toolbar = findViewById(R.id.toolbar_main)
        btnSelectCity = findViewById(R.id.btn_select_city)
        btnGetCoorginates = findViewById(R.id.btn_get_location)
        btnClearLatitude = findViewById(R.id.btn_clear_latitude)
        btnClearLongitude = findViewById(R.id.btn_clear_longitude)
        btnGetWeather = findViewById(R.id.btn_get_weather)
        btnInfo = findViewById(R.id.info_image_btn)
    }

    private fun setOnClickListeners() {
        btnSelectCity.setOnClickListener(this)
        btnGetCoorginates.setOnClickListener(this)
        btnClearLongitude.setOnClickListener(this)
        btnClearLatitude.setOnClickListener(this)
        btnGetWeather.setOnClickListener(this)
        btnInfo.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_select_city -> openCitiesListActivity()
            R.id.btn_get_location -> getDeviceLocation()
            R.id.btn_get_weather -> getWeather()
            R.id.info_image_btn -> showProgramInfo()
            R.id.btn_clear_latitude -> latitudeField.setText("")
            R.id.btn_clear_longitude -> longitudeField.setText("")
        }
        hideSoftKeyboard()
    }

    private fun setTextWatcherForCoordinatesFields() {

        latitudeField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (charSequence.isNotEmpty()) {
                    btnClearLatitude.visibility = View.VISIBLE
                } else {
                    btnClearLatitude.visibility = View.INVISIBLE
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
        })

        longitudeField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                if (charSequence.isNotEmpty()) {
                    btnClearLongitude.visibility = View.VISIBLE
                } else {
                    btnClearLongitude.visibility = View.INVISIBLE
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
        })
    }

    private fun round(number: Float): Float {
        val i = 1000
        return (number * i).roundToInt().toFloat() / i
    }

    private fun hideSoftKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}