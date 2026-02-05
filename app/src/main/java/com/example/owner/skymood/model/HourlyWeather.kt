package com.example.owner.skymood.model

import android.graphics.Bitmap

/**
 * Created by owner on 04/04/2016.
 */
data class HourlyWeather(
    val hour: String?,
    val condition: String?,
    val temp: String?,
    val icon: Bitmap?
)
