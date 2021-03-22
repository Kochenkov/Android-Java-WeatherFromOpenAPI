package com.vkochenkov.weatherfromopenapis.presentation.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.presentation.viewmodel.CitiesListViewModel

class CityDeleteDialog(
    context: Context,
    private val viewModel: CitiesListViewModel,
    private val city: City
) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_delete_city)
        findViewById<View>(R.id.apply_delete_city_btn).setOnClickListener {
            viewModel.deleteCityFromDb(city)

            Toast.makeText(
                context,
                "${city.name} удален из списка", Toast.LENGTH_SHORT
            ).show()

            onBackPressed()
        }
        findViewById<View>(R.id.cancel_delete_city_btn).setOnClickListener { onBackPressed() }
    }
}