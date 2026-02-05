package com.example.owner.skymood.model

import android.content.Context
import com.example.owner.skymood.model.DAO.MyLocationDAO

/**
 * Created by owner on 05/04/2016.
 */
class MyLocationManager private constructor(context: Context) {
    private val locationDAO: MyLocationDAO = MyLocationDAO.getInstance(context)

    val allMyLocations: ArrayList<MyLocation>
        get() = locationDAO.allMyLocations

    fun insertMyLocation(location: MyLocation): Long {
        return locationDAO.insertMyLocation(location)
    }

    fun selectMyLocation(location: MyLocation): MyLocation? {
        return locationDAO.selectMyLocation(location)
    }

    fun deleteMyLocation(location: MyLocation): Long {
        return locationDAO.deleteMyLocation(location)
    }

    val allStringLocations: ArrayList<String>
        get() = locationDAO.allStringLocations

    companion object {
        private var ourInstance: MyLocationManager? = null
        @JvmStatic
        fun getInstance(context: Context): MyLocationManager {
            if (ourInstance == null) {
                ourInstance = MyLocationManager(context)
            }
            return ourInstance!!
        }
    }
}
