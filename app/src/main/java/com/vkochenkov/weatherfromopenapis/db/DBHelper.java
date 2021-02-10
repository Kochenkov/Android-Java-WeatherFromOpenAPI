package com.vkochenkov.weatherfromopenapis.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.vkochenkov.weatherfromopenapis.entities.CitiesArrayData;
import com.vkochenkov.weatherfromopenapis.entities.City;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CitiesDatabase";
    public static final String CITIES_TABLE = "CitiesTable";

    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CITIES_TABLE + "(" +
                           KEY_NAME + " text primary key," +
                           KEY_LATITUDE + " text," +
                           KEY_LONGITUDE + " text" + ")");

        for (int i=0; i< CitiesArrayData.getCitiesArrayList().size(); i++) {
            City city = CitiesArrayData.getCitiesArrayList().get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(KEY_NAME, city.getName());
            contentValues.put(KEY_LATITUDE, city.getLatitude());
            contentValues.put(KEY_LONGITUDE,city.getLongitude());
            db.insert(CITIES_TABLE, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + CITIES_TABLE);
        onCreate(db);
    }

    public void deleteElement(SQLiteDatabase db, String name) {
        db.execSQL("DELETE FROM " + CITIES_TABLE + " WHERE " +  KEY_NAME + " = \"" + name + "\"");
    }
}
