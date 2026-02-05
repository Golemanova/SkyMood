package com.example.owner.skymood.model

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Golemanovaa on 5.4.2016 г..
 */
class LocationPreference private constructor(context: Context) {
    private val pref: SharedPreferences
    private val editor: SharedPreferences.Editor
    var PRIVATE_MODE: Int = 0

    init {
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun setPreferredLocation(
        city: String?,
        country: String?,
        countryCode: String?,
        icon: String?,
        temperature: String?,
        minTemp: String?,
        maxTemp: String?,
        condition: String?,
        feelsLike: String?,
        lastUpdate: String?
    ) {
        editor.putString(CITY, city)
        editor.putString(COUNTRY, country)
        editor.putString(COUNTRY_CODE, countryCode)
        editor.putString(ICON, icon)
        editor.putString(TEMPERATURE, temperature)
        editor.putString(MIN_TEMP, minTemp)
        editor.putString(MAX_TEMP, maxTemp)
        editor.putString(CONDITION, condition)
        editor.putString(FEELS_LIKE, feelsLike)
        editor.putString(LAST_UPDATE, lastUpdate)
        editor.commit()
        MyWidgedProvider.setInfo(city, country, countryCode)
    }

    val isSetLocation: Boolean
        get() = pref.contains(CITY)

    val city: String?
        get() = pref.getString(CITY, null)

    val country: String?
        get() = pref.getString(COUNTRY, null)

    val countryCode: String?
        get() = pref.getString(COUNTRY_CODE, null)

    val icon: String?
        get() = pref.getString(ICON, null)

    val temperature: String?
        get() = pref.getString(TEMPERATURE, null)

    val minTemp: String?
        get() = pref.getString(MIN_TEMP, null)

    val maxTemp: String?
        get() = pref.getString(MAX_TEMP, null)

    val condition: String?
        get() = pref.getString(CONDITION, null)

    val feelsLike: String?
        get() = pref.getString(FEELS_LIKE, null)

    val lastUpdate: String?
        get() = pref.getString(LAST_UPDATE, null)

    fun hasNull(): Boolean {
        return this.city == null || this.country == null || this.countryCode == null || this.icon == null || this.temperature == null || this.minTemp == null || this.maxTemp == null || this.condition == null || this.feelsLike == null || this.lastUpdate == null
    }

    fun removeInfo() {
        editor.clear()
        editor.commit()
    }

    companion object {
        private const val PREFER_NAME = "SkyModePreferences"
        const val CITY: String = "city"
        const val COUNTRY: String = "fragment_current_weather_tv_country"
        const val COUNTRY_CODE: String = "countryCode"
        const val ICON: String = "widget_layout_iv_icon"
        const val TEMPERATURE: String = "temperature"
        const val MIN_TEMP: String = "fragment_current_weather_tv_min_temp"
        const val MAX_TEMP: String = "fragment_current_weather_tv_max_temp"
        const val CONDITION: String = "widget_layout_tv_condition"
        const val FEELS_LIKE: String = "feelsLike"
        const val LAST_UPDATE: String = "lastUpdate"

        // @Volatile ensures that the value of instance is always up-to-date and
        // the same on all execution threads.
        @Volatile
        private var instance: LocationPreference? = null

        fun getInstance(context: Context): LocationPreference {
            // Double-checked locking for thread safety
            return instance ?: synchronized(this) {
                instance ?: LocationPreference(context).also { instance = it }
            }
        }
    }
}
