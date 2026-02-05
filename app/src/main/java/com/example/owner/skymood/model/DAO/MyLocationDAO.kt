package com.example.owner.skymood.model.DAO

import android.content.ContentValues
import android.content.Context
import com.example.owner.skymood.model.DatabaseHelper
import com.example.owner.skymood.model.MyLocation

/**
 * Created by owner on 05/04/2016.
 */
class MyLocationDAO private constructor(context: Context) : IMyLocationDAO {
    private val helper: DatabaseHelper = DatabaseHelper.getInstance(context)

    override val allMyLocations: ArrayList<MyLocation>
        get() {
            val db = helper.readableDatabase

            val columns: Array<String> = arrayOf(
                DatabaseHelper.LOCATION_ID,
                DatabaseHelper.CITY,
                DatabaseHelper.COUNTRY,
                DatabaseHelper.COUNTRY_CODE,
                DatabaseHelper.LOCATION
            )
            val c = db.query(DatabaseHelper.MY_LOCATIONS, columns, null, null, null, null, null)
            val cities = ArrayList<MyLocation>()
            if (c.moveToFirst()) {
                do {
                    cities.add(
                        MyLocation(
                            id = c.getLong(c.getColumnIndex(DatabaseHelper.LOCATION_ID)),
                            city = c.getString(c.getColumnIndex(DatabaseHelper.CITY)),
                            code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE)),
                            country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY)),
                            location = c.getString(c.getColumnIndex(DatabaseHelper.LOCATION))
                        )
                    )
                } while (c.moveToNext())
            }
            c.close()
            db.close()
            return cities
        }

    override fun insertMyLocation(location: MyLocation): Long {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(DatabaseHelper.CITY, location.city)
        values.put(DatabaseHelper.COUNTRY, location.country)
        values.put(DatabaseHelper.COUNTRY_CODE, location.code)
        values.put(DatabaseHelper.LOCATION, location.location)
        var id: Long = -1

        if (selectMyLocation(location) == null)
            id = db.insert(DatabaseHelper.MY_LOCATIONS, null, values)

        db.close()
        return id
    }

    override fun selectMyLocation(location: MyLocation): MyLocation? {
        val db = helper.readableDatabase

        val columns: Array<String> = arrayOf(
            DatabaseHelper.LOCATION_ID,
            DatabaseHelper.CITY,
            DatabaseHelper.COUNTRY,
            DatabaseHelper.COUNTRY_CODE,
            DatabaseHelper.LOCATION
        )
        val selection = DatabaseHelper.CITY + " = ? AND " + DatabaseHelper.COUNTRY + " = ?"
        val c = db.query(
            DatabaseHelper.MY_LOCATIONS,
            columns,
            selection,
            arrayOf(location.city, location.country),
            null,
            null,
            null
        )

        return if (c.moveToFirst()) {
            MyLocation(
                id = c.getLong(c.getColumnIndex(DatabaseHelper.LOCATION_ID)),
                city = c.getString(c.getColumnIndex(DatabaseHelper.CITY)),
                code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE)),
                country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY)),
                location = c.getString(c.getColumnIndex(DatabaseHelper.LOCATION))
            )
        } else {
            null
        }
    }

    override fun deleteMyLocation(location: MyLocation): Long {
        val db = helper.readableDatabase
        val id = db.delete(
            DatabaseHelper.MY_LOCATIONS,
            DatabaseHelper.CITY + " = ? AND " + DatabaseHelper.COUNTRY + " = ?",
            arrayOf<String?>(location.city, location.country)
        ).toLong()
        db.close()
        return id
    }

    override val allStringLocations: ArrayList<String>
        get() {
            val db = helper.readableDatabase
            val c = db.query(
                DatabaseHelper.MY_LOCATIONS,
                arrayOf<String>(DatabaseHelper.LOCATION),
                null,
                null,
                null,
                null,
                null
            )
            val locations = ArrayList<String>()
            if (c.moveToFirst()) {
                do {
                    locations.add(c.getString(c.getColumnIndex(DatabaseHelper.LOCATION)))
                } while (c.moveToNext())
            }
            c.close()
            db.close()
            return locations
        }


    companion object {
        private var instance: MyLocationDAO? = null
        fun getInstance(context: Context): MyLocationDAO {
            if (instance == null) instance = MyLocationDAO(context)
            return instance!!
        }
    }
}
