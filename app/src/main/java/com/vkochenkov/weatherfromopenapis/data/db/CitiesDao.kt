package com.vkochenkov.weatherfromopenapis.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vkochenkov.weatherfromopenapis.data.db.entities.City

@Dao
interface CitiesDao {

    @Query("SELECT * FROM Cities")
    fun getAllCities() : List<City>
}