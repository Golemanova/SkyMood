package com.example.owner.skymood.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Created by owner on 05/04/2016.
 */
@Parcelize
data class SearchedLocation(
    var id: Long = 0,
    val city: String?,
    val temp: String?,
    val condition: String?,
    var date: String? = null,
    val country: String?,
    var code: String? = null,
    val max: String?,
    val min: String?,
    val lastUpdate: String?,
    val icon: String?,
    val feelsLike: String?
) : Parcelable
