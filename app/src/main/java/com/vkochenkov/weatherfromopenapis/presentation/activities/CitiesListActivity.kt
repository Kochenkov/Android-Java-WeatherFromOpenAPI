package com.vkochenkov.weatherfromopenapis.presentation.activities

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.vkochenkov.weatherfromopenapis.R
import com.vkochenkov.weatherfromopenapis.data.db.entities.City
import com.vkochenkov.weatherfromopenapis.presentation.CitiesListViewModel
import com.vkochenkov.weatherfromopenapis.presentation.MainScreenViewModel
import com.vkochenkov.weatherfromopenapis.presentation.dialogs.CityAddDialog
import com.vkochenkov.weatherfromopenapis.presentation.dialogs.CityDeleteDialog
import com.vkochenkov.weatherfromopenapis.presentation.recycler.CityClickListener
import com.vkochenkov.weatherfromopenapis.presentation.recycler.CityListAdapter

class CitiesListActivity : AppCompatActivity() {

    private val viewModel: CitiesListViewModel by lazy {
        ViewModelProvider(this).get(CitiesListViewModel::class.java)
    }

    private var toolbar: Toolbar? = null
    private var citiesSearchField: EditText? = null
    private var cityListView: RecyclerView? = null
    private var addCityBtn: Button? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cities_list)
        initFields()
        initRecycler()
        setAddCityBtnClickListener()
        initObserveForViewModel()
        viewModel.getAllCities()
       // setTextWatcherForSearchCity()
        toolbar!!.setNavigationOnClickListener { onBackPressed() }
    }

    private fun initObserveForViewModel() {
        viewModel.citiesLiveData.observe(this, Observer {
            (cityListView!!.adapter as CityListAdapter).setData(it)
        })
    }

    private fun initRecycler() {
        cityListView!!.layoutManager = LinearLayoutManager(this)
        val cityClickListener: CityClickListener = object : CityClickListener {
            override fun onCityClickListener(city: City?): View.OnClickListener {
                return View.OnClickListener {
                    val intent = Intent()
                         intent.putExtra("latitude", city?.latitude);
                         intent.putExtra("longitude", city?.longitude);
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }
            }

            override fun onCityLongClickListener(
                position: Int,
                city: City?
            ): OnLongClickListener {
                return OnLongClickListener {
                        // var dialog = CityDeleteDialog(CitiesListActivity.this, cityListView, city)
                       //  dialog.show();
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
        addCityBtn!!.setOnClickListener {
            val dialog = CityAddDialog(this@CitiesListActivity)
//                object : CityAddDialog.AddCityCallback {
//                override fun added(flag: Boolean) {
//                    if (flag) {
//                        cityListView!!.adapter?.notifyDataSetChanged()
//                    }
//                }
//            })
            dialog.show()
        }
    }

//    private fun setTextWatcherForSearchCity() {
//        citiesSearchField!!.addTextChangedListener(object : TextWatcher {
//            override fun onTextChanged(
//                charSequence: CharSequence,
//                i: Int,
//                i1: Int,
//                i2: Int
//            ) {
//                val adapter = cityListView!!.adapter as CityListAdapter?
//                adapter?.findAndShowCityItems(charSequence.toString())
//            }
//
//            override fun beforeTextChanged(
//                charSequence: CharSequence,
//                i: Int,
//                i1: Int,
//                i2: Int
//            ) {
//            }
//
//            override fun afterTextChanged(editable: Editable) {}
//        })
//    }
}