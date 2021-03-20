package com.vkochenkov.weatherfromopenapis.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.presentation.CitiesListViewModel

class CityAddDialog(
    context: Context,
    private val viewModel: CitiesListViewModel
) :
    Dialog(context) {

    private var cityNameEdt: EditText? = null
    private var latitudeEdt: EditText? = null
    private var longitudeEdt: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_city)
        initFields()
        findViewById<View>(R.id.apply_add_city_btn).setOnClickListener {

            val city = City(
                cityNameEdt?.getText().toString(),
                latitudeEdt?.getText().toString(),
                longitudeEdt?.getText().toString()
            )

            viewModel.insertCityToDb(city)
            onBackPressed()
        }
        findViewById<View>(R.id.cancel_add_city_btn).setOnClickListener {
            onBackPressed()
        }
    }

    private fun initFields() {
        cityNameEdt = findViewById(R.id.add_city_name_edt)
        latitudeEdt = findViewById(R.id.add_latitude_edt)
        longitudeEdt = findViewById(R.id.add_longitude_edt)
    }
}