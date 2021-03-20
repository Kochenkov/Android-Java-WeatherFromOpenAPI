package com.vkochenkov.weatherfromopenapis.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vkochenkov.weatherfromopenapis.data.db.entities.City

@Database(entities = [(City::class)], version = 1)
abstract class CitiesDb: RoomDatabase() {

    abstract fun citiesDao(): CitiesDao
}