package com.vkochenkov.weatherfromopenapis.data.db

import androidx.room.*
import com.vkochenkov.weatherfromopenapis.data.db.entities.City

@Dao
interface CitiesDao {

    @Query("SELECT * FROM Cities")
    fun getAllCities() : List<City>

    @Insert
    fun insertCities(cities: List<City>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCity(city: City)

    @Delete
    fun deleteCity(city: City)
}