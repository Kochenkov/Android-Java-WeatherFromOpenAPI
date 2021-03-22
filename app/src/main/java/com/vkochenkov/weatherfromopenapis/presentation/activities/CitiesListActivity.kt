package com.vkochenkov.weatherfromopenapis.presentation.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.presentation.dialogs.CityAddDialog
import com.vkochenkov.weatherfromopenapis.presentation.dialogs.CityDeleteDialog
import com.vkochenkov.weatherfromopenapis.presentation.recycler.CityClickListener
import com.vkochenkov.weatherfromopenapis.presentation.recycler.CityListAdapter
import com.vkochenkov.weatherfromopenapis.presentation.viewmodel.CitiesListViewModel

class CitiesListActivity : AppCompatActivity() {

    private val viewModel: CitiesListViewModel by lazy {
        ViewModelProvider(this).get(CitiesListViewModel::class.java)
    }

    private lateinit var toolbar: Toolbar
    private lateinit var citiesSearchField: EditText
    private lateinit var cityListView: RecyclerView
    private lateinit var addCityBtn: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities_list)
        initFields()
        initRecycler()
        setAddCityBtnClickListener()
        initObserveForViewModel()

        viewModel.getAllCitiesFromDb()
        setTextWatcherForSearchCity()
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initObserveForViewModel() {
        viewModel.citiesLiveData.observe(this, Observer {
            (cityListView.adapter as CityListAdapter).setData(it)
        })
    }

    private fun initRecycler() {
        cityListView.layoutManager = LinearLayoutManager(this)
        val cityClickListener: CityClickListener = object : CityClickListener {

            override fun onCityClickListener(city: City): View.OnClickListener {
                return View.OnClickListener {
                    val intent = Intent()
                    intent.putExtra("latitude", city.latitude);
                    intent.putExtra("longitude", city.longitude);
                    setResult(Activity.RESULT_OK, intent)
                    Toast.makeText(
                        getApplicationContext(),
                        "${city.name}", Toast.LENGTH_SHORT
                    ).show()
                    finish()
                }
            }

            override fun onCityLongClickListener(position: Int, city: City): OnLongClickListener {
                return OnLongClickListener {
                    var dialog = CityDeleteDialog(this@CitiesListActivity, viewModel, city)
                    dialog.show()
                    true
                }
            }
        }
        cityListView!!.adapter = CityListAdapter(cityClickListener)
        val dividerItemDecoration = DividerItemDecoration(
            this,
            this.resources.configuration.orientation
        )
        cityListView!!.addItemDecoration(dividerItemDecoration)
    }

    private fun initFields() {
        toolbar = findViewById(R.id.toolbar_cities_list)
        citiesSearchField = findViewById(R.id.edt_city_search_field)
        cityListView = findViewById(R.id.recycler_city_list)
        addCityBtn = findViewById(R.id.add_city_btn)
    }

    private fun setAddCityBtnClickListener() {
        addCityBtn.setOnClickListener {
            val dialog = CityAddDialog(this@CitiesListActivity, viewModel)
            dialog.show()
        }
    }

    private fun setTextWatcherForSearchCity() {
        citiesSearchField.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
                viewModel.getSameNameCitiesFromDb(charSequence.toString())
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
}