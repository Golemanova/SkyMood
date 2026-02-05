package com.example.owner.skymood.model

import android.content.Context
import com.example.owner.skymood.model.DAO.SearchedLocationsDAO

/**
 * Created by owner on 05/04/2016.
 */
class SearchedLocationManager private constructor(context: Context) {
    var locationsDAO: SearchedLocationsDAO = SearchedLocationsDAO.getInstance(context.applicationContext)

    val allSearchedLocations: ArrayList<SearchedLocation>
        get() = locationsDAO.allSearchedLocations

    fun insertSearchedLocation(location: SearchedLocation) =
        locationsDAO.insertSearchedLocation(location)

    companion object {
        // @Volatile guarantees that writes to this field are immediately visible to other threads.
        @Volatile
        private var instance: SearchedLocationManager? = null

        fun getInstance(context: Context): SearchedLocationManager {
            // The double-checked lock prevents multiple instances from being created in a multi-threaded environment.
            return instance ?: synchronized(this) {
                instance ?: SearchedLocationManager(context).also { instance = it }
            }
        }
    }
}
