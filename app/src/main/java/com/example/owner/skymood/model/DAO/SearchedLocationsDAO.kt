package com.example.owner.skymood.model.DAO

import android.content.ContentValues
import android.content.Context
import com.example.owner.skymood.model.DatabaseHelper
import com.example.owner.skymood.model.SearchedLocation
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Created by owner on 05/04/2016.
 */
class SearchedLocationsDAO private constructor(context: Context) : ISearchedLocations {
    var helper: DatabaseHelper = DatabaseHelper.getInstance(context)

    override val allSearchedLocations: ArrayList<SearchedLocation>
        get() {
            val db = helper.readableDatabase
            val columns: Array<String> = arrayOf(
                DatabaseHelper.SEARCHED_ID,
                DatabaseHelper.CITY,
                DatabaseHelper.TEMP,
                DatabaseHelper.CONDITION,
                DatabaseHelper.DATE,
                DatabaseHelper.COUNTRY,
                DatabaseHelper.COUNTRY_CODE,
                DatabaseHelper.MAX_TEMP,
                DatabaseHelper.MIN_TEMP,
                DatabaseHelper.LAST_UPDATE,
                DatabaseHelper.ICON,
                DatabaseHelper.FEELS_LIKE
            )
            val c = db.query(DatabaseHelper.LAST_SEARCHED, columns, null, null, null, null, null)
            val cities = ArrayList<SearchedLocation>()
            if (c.moveToFirst()) do {
                val location = SearchedLocation(
                    id = c.getLong(c.getColumnIndex(DatabaseHelper.SEARCHED_ID)),
                    city = c.getString(c.getColumnIndex(DatabaseHelper.CITY)),
                    temp = c.getString(c.getColumnIndex(DatabaseHelper.TEMP)),
                    condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION)),
                    date = c.getString(c.getColumnIndex(DatabaseHelper.DATE)),
                    country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY)),
                    code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE)),
                    max = c.getString(c.getColumnIndex(DatabaseHelper.MAX_TEMP)),
                    min = c.getString(c.getColumnIndex(DatabaseHelper.MIN_TEMP)),
                    lastUpdate = c.getString(c.getColumnIndex(DatabaseHelper.LAST_UPDATE)),
                    icon = c.getString(c.getColumnIndex(DatabaseHelper.ICON)),
                    feelsLike = c.getString(c.getColumnIndex(DatabaseHelper.FEELS_LIKE))
                )
                cities.add(location)
            } while (c.moveToNext())
            c.close()
            db.close()
            return cities
        }

    override fun insertSearchedLocation(location: SearchedLocation): Long {
        var id = checkCity(location.city)
        if (id != -1L) {
            return updateLocation(id, location)
        } else if (count < 5) {
            return insertLocation(location)
        } else {
            id = selectFirstSearchedCity()!!.id
            return updateLocation(id, location)
        }
    }

    override fun selectFirstSearchedCity(): SearchedLocation? {
        val db = helper.readableDatabase

        val columns: Array<String> = arrayOf(
            DatabaseHelper.SEARCHED_ID,
            DatabaseHelper.CITY,
            DatabaseHelper.TEMP,
            DatabaseHelper.CONDITION,
            DatabaseHelper.DATE,
            DatabaseHelper.COUNTRY,
            DatabaseHelper.COUNTRY_CODE,
            DatabaseHelper.MAX_TEMP,
            DatabaseHelper.MIN_TEMP,
            DatabaseHelper.LAST_UPDATE,
            DatabaseHelper.ICON,
            DatabaseHelper.FEELS_LIKE
        )
        val c = db.query(
            DatabaseHelper.LAST_SEARCHED,
            columns,
            null,
            null,
            null,
            null,
            "datetime(" + DatabaseHelper.DATE + ")",
            "1"
        )
        var location: SearchedLocation? = null
        if (c.moveToFirst()) do {
            location = SearchedLocation(
                id = c.getLong(c.getColumnIndex(DatabaseHelper.SEARCHED_ID)),
                city = c.getString(c.getColumnIndex(DatabaseHelper.CITY)),
                temp = c.getString(c.getColumnIndex(DatabaseHelper.TEMP)),
                condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION)),
                date = c.getString(c.getColumnIndex(DatabaseHelper.DATE)),
                country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY)),
                code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE)),
                max = c.getString(c.getColumnIndex(DatabaseHelper.MAX_TEMP)),
                min = c.getString(c.getColumnIndex(DatabaseHelper.MIN_TEMP)),
                lastUpdate = c.getString(c.getColumnIndex(DatabaseHelper.LAST_UPDATE)),
                icon = c.getString(c.getColumnIndex(DatabaseHelper.ICON)),
                feelsLike = c.getString(c.getColumnIndex(DatabaseHelper.FEELS_LIKE))
            )
        } while (c.moveToNext())
        c.close()
        db.close()
        return location
    }

    override val count: Long
        get() {
            val db = helper.readableDatabase
            val query = "SELECT COUNT(*) FROM " + DatabaseHelper.LAST_SEARCHED
            val statement = db.compileStatement(query)
            val count = statement.simpleQueryForLong()
            db.close()
            return count
        }

    override fun checkCity(city: String?): Long {
        val db = helper.readableDatabase


        val selection = DatabaseHelper.CITY + " = ?"
        val c = db.query(
            DatabaseHelper.LAST_SEARCHED,
            arrayOf<String>(DatabaseHelper.SEARCHED_ID, DatabaseHelper.CITY),
            selection,
            arrayOf<String?>(city),
            null,
            null,
            null
        )
        if (c.moveToFirst()) {
            val id = c.getLong(c.getColumnIndex(DatabaseHelper.SEARCHED_ID))
            c.close()
            db.close()
            return id
        } else {
            c.close()
            db.close()
            return -1
        }
    }

    override fun insertLocation(location: SearchedLocation): Long {
        val db = helper.writableDatabase
        val values = ContentValues()
        values.put(DatabaseHelper.CITY, location.city)
        values.put(DatabaseHelper.TEMP, location.temp)
        values.put(DatabaseHelper.CONDITION, location.condition)
        values.put(DatabaseHelper.MAX_TEMP, location.max)
        values.put(DatabaseHelper.MIN_TEMP, location.min)
        values.put(DatabaseHelper.LAST_UPDATE, location.lastUpdate)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = sdf.format(Date())
        values.put(DatabaseHelper.DATE, strDate)
        values.put(DatabaseHelper.ICON, location.icon)
        values.put(DatabaseHelper.FEELS_LIKE, location.feelsLike)
        val id = db.insert(DatabaseHelper.LAST_SEARCHED, null, values)

        db.close()
        return id
    }

    override fun updateLocation(id: Long, location: SearchedLocation): Long {
        val db = helper.getWritableDatabase()
        val values = ContentValues()
        values.put(DatabaseHelper.CITY, location.city)
        values.put(DatabaseHelper.TEMP, location.temp)
        values.put(DatabaseHelper.CONDITION, location.condition)
        values.put(DatabaseHelper.COUNTRY, location.country)
        values.put(DatabaseHelper.COUNTRY_CODE, location.code)
        values.put(DatabaseHelper.MAX_TEMP, location.max)
        values.put(DatabaseHelper.MIN_TEMP, location.min)
        values.put(DatabaseHelper.LAST_UPDATE, location.lastUpdate)
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val strDate = sdf.format(Date())
        values.put(DatabaseHelper.DATE, strDate)
        values.put(DatabaseHelper.ICON, location.icon)
        values.put(DatabaseHelper.FEELS_LIKE, location.feelsLike)
        val result = db.update(
            DatabaseHelper.LAST_SEARCHED,
            values,
            DatabaseHelper.SEARCHED_ID + " = ? ",
            arrayOf("" + id)
        ).toLong()

        db.close()
        return result
    }


    companion object {
        private var instance: SearchedLocationsDAO? = null
        fun getInstance(context: Context): SearchedLocationsDAO {
            if (instance == null) instance = SearchedLocationsDAO(context)
            return instance!!
        }
    }
}
