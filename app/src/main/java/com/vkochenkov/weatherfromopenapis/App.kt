package com.vkochenkov.weatherfromopenapis

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vkochenkov.weatherfromopenapis.data.CitiesArrayData
import com.vkochenkov.weatherfromopenapis.data.db.CitiesDb
import com.vkochenkov.weatherfromopenapis.data.weather_api.WeatherApiService
import com.vkochenkov.weatherfromopenapis.data.weather_api.WeatherApiService.Companion.BASE_URL
import com.vkochenkov.weatherfromopenapis.domain.Repository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.Executors


class App : Application() {

    lateinit var apiService: WeatherApiService
    lateinit var database: CitiesDb
    val repository: Repository = Repository()

    companion object {
        var instance: App? = null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        initRetrofit()
        initDatabase()
    }

    private fun initRetrofit() {
        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL + BuildConfig.API_ACCESS_KEY + "/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }

    private fun initDatabase() {
        database = Room.databaseBuilder(applicationContext, CitiesDb::class.java, "cities_db")
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    //pre-populate data
                    Executors.newSingleThreadExecutor().execute {
                        instance?.let {
                            it.database.citiesDao().insertCities(CitiesArrayData.getCitiesArrayList())
                        }
                    }
                }
            })
            .allowMainThreadQueries()
            .build()
    }

}