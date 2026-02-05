package com.example.owner.skymood.model.DAO

import com.example.owner.skymood.model.SearchedLocation

/**
 * Created by owner on 05/04/2016.
 */
interface ISearchedLocations {
    val allSearchedLocations: ArrayList<SearchedLocation>?
    fun insertSearchedLocation(location: SearchedLocation): Long
    fun selectFirstSearchedCity(): SearchedLocation?
    val count: Long
    fun checkCity(city: String?): Long
    fun insertLocation(location: SearchedLocation): Long
    fun updateLocation(id: Long, location: SearchedLocation): Long
}
