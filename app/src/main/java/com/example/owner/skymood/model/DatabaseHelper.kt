package com.example.owner.skymood.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by owner on 05/04/2016.
 */
class DatabaseHelper private constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_MY_LOCATIONS)
        db.execSQL(CREATE_LAST_SEARCHED)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $MY_LOCATIONS")
        db.execSQL("DROP TABLE IF EXISTS $LAST_SEARCHED")

        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "SKY_MOOD_DATABASE"
        private const val DATABASE_VERSION = 14

        //tables
        const val MY_LOCATIONS: String = "my_locations"
        const val LAST_SEARCHED: String = "last_searched"

        const val CITY: String = "city"
        const val LOCATION_ID: String = "id"
        const val LOCATION: String = "location"

        // last searched
        const val SEARCHED_ID: String = "id"
        const val TEMP: String = "temp"
        const val CONDITION: String = "condition"
        const val DATE: String = "date_time"
        const val COUNTRY: String = "country"
        const val COUNTRY_CODE: String = "country_code"
        const val ICON: String = "icon"
        const val MAX_TEMP: String = "max_temp"
        const val MIN_TEMP: String = "min_temp"
        const val LAST_UPDATE: String = "last_update"
        const val FEELS_LIKE: String = "feels_like"

        //create table statements
        private const val CREATE_MY_LOCATIONS = ("CREATE TABLE IF NOT EXISTS " + MY_LOCATIONS + " ("
                + LOCATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + CITY + " VARCHAR(30) NOT NULL, "
                + COUNTRY + " VARCHAR(30) NOT NULL, "
                + COUNTRY_CODE + " VARCHAR(30) NOT NULL, "
                + LOCATION + " VARCHAR(80) NOT NULL)")

        private const val CREATE_LAST_SEARCHED = ("CREATE TABLE IF NOT EXISTS " + LAST_SEARCHED + " ("
                + SEARCHED_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
                + CITY + " VARCHAR(30) NOT NULL, "
                + TEMP + " text NOT NULL, "
                + CONDITION + " text NOT NULL, "
                + DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, "
                + COUNTRY + " text NOT NULL, "
                + COUNTRY_CODE + " text NOT NULL, "
                + ICON + " text NOT NULL, "
                + MAX_TEMP + " text NOT NULL, "
                + MIN_TEMP + " text NOT NULL, "
                + LAST_UPDATE + " text NOT NULL, "
                + FEELS_LIKE + " text NOT NULL "
                + ") ")

        @Volatile
        private var instance: DatabaseHelper? = null

        /**
         * Returns the singleton instance of DatabaseHelper.
         * Uses double-checked locking for thread safety.
         */
        @JvmStatic
        fun getInstance(context: Context): DatabaseHelper {
            return instance ?: synchronized(this) {
                instance ?: DatabaseHelper(context).also { instance = it }
            }
        }
    }
}
