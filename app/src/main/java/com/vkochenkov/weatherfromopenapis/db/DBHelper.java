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

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CITIES_TABLE + "(" +
                           KEY_ID + " integer primary key," +
                           KEY_NAME + " text," +
                           KEY_LATITUDE + " text," +
                           KEY_LONGITUDE + " text" + ")");

//        db.execSQL("insert into " + CITIES_TABLE + "(" + KEY_NAME + "," + KEY_LATITUDE + "," + KEY_LONGITUDE + ")" +
//                " values " + "(" + "Санкт-Петербург" + ","  + "59.939095" + "," + "30.315868" +")");


        for (int i=0; i< CitiesArrayData.getCitiesArrayList().size(); i++) {
            City city = CitiesArrayData.getCitiesArrayList().get(i);
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_NAME, city.getName());
            contentValues.put(DBHelper.KEY_LATITUDE, city.getLatitude());
            contentValues.put(DBHelper.KEY_LONGITUDE,city.getLongitude());
            db.insert(DBHelper.CITIES_TABLE, null, contentValues);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists " + CITIES_TABLE);
        onCreate(db);
    }
}
