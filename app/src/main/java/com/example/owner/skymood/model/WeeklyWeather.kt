package com.example.owner.skymood.model

import android.graphics.Bitmap

/**
 * Created by owner on 05/04/2016.
 */
data class WeeklyWeather(
    val day: String?,
    val min: String?,
    val max: String?,
    val condition: String?,
    val icon: Bitmap?
)
