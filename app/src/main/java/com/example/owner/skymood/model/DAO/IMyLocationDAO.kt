package com.example.owner.skymood.model.DAO

import com.example.owner.skymood.model.MyLocation

/**
 * Created by owner on 05/04/2016.
 */
interface IMyLocationDAO {
    val allMyLocations: ArrayList<MyLocation>?
    fun insertMyLocation(location: MyLocation): Long
    fun selectMyLocation(location: MyLocation): MyLocation?
    fun deleteMyLocation(location: MyLocation): Long
    val allStringLocations: ArrayList<String>?
}
