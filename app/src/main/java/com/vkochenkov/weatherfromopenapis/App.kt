package com.vkochenkov.weatherfromopenapis

import android.app.Application
import android.content.ContentValues
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
                    //при самом первом запуске, добавляем города в базу из списка
                    for (i in 0 until CitiesArrayData.getCitiesArrayList().size) {
                        val city = CitiesArrayData.getCitiesArrayList().get(i)
                        val contentValues = ContentValues()
                        contentValues.put("name", city.name);
                        contentValues.put("latitude", city.latitude);
                        contentValues.put("longitude", city.longitude);
                        db.insert("Cities", 0, contentValues)
                    }
                }
            })
            .allowMainThreadQueries()
            .build()
    }
}