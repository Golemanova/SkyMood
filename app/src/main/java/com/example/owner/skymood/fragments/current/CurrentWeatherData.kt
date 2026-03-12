package com.example.owner.skymood.fragments.current

import com.example.owner.skymood.R.drawable
import com.example.owner.skymood.data.responses.ForecastResponse

/**
 * UI-friendly data model for current weather information used in [CurrentWeatherFragment].
 */
data class CurrentWeatherData(
    val cityName: String,
    val country: String,
    val tempC: String,
    val feelsLikeC: String,
    val minTempC: String,
    val maxTempC: String,
    val conditionText: String,
    val imageRes: Int,
    val isNight: Boolean,
    val lastUpdate: String,
)

/**
 * Mapper to convert [ForecastResponse] from the API to [CurrentWeatherData] for the UI.
 */
fun ForecastResponse.toCurrentWeatherData(): CurrentWeatherData {
    val forecastDay = forecast.forecastDay.firstOrNull()
    
    return CurrentWeatherData(
        cityName = location.name,
        country = location.country,
        tempC =  "${current.tempC.toInt()}°",
        feelsLikeC = "Feels like: ${current.feelsLikeC.toInt()}°",
        minTempC = "⬇${forecastDay?.day?.minTempC?.toInt() ?: "N/A"}°" ,
        maxTempC =  "⬆${forecastDay?.day?.maxTempC?.toInt() ?: "N/A"}°" ,
        conditionText = current.condition.text,
        imageRes = current.condition.code.getImageResource(current.isDay == 0),
        isNight = current.isDay == 0,
        lastUpdate = "Last update: ${current.lastUpdated}",
    )
}

private fun Int.getImageResource(isNight: Boolean) = when (this) {
    1000 -> if (isNight) drawable.sunny_night else drawable.sunny

    1003 -> if (isNight) drawable.partlycloudy_night else drawable.partlycloudy

    1006 -> if (isNight) drawable.mostlycloudy_night else drawable.mostlycloudy

    1009 -> if (isNight) drawable.cloudy_night else drawable.cloudy

    1030, 1135, 1147 -> if (isNight) drawable.fog_night else drawable.fog

    1063 -> if (isNight) drawable.chancerain_night else drawable.chancerain

    1072, 1066 -> if (isNight) drawable.chancesnow_night else drawable.chancesnow

    1069 -> if (isNight) drawable.chancesleet_night else drawable.chancesleet

    1273, 1276, 1279, 1282, 1087 -> if (isNight) drawable.tstorms_night else drawable.tstorms

    1114, 1210, 1213, 1216, 1219, 1222, 1225,
    1237, 1255, 1258, 1261, 1264, 1171, 1117 -> if (isNight) drawable.snow_night else drawable.snow

    1150, 1168, 1153 -> if (isNight) drawable.hazy_night else drawable.hazy

    1180, 1186, 1198, 1189, 1192, 1201,
    1240, 1243, 1246, 1183 -> if (isNight) drawable.rain_night else drawable.rain

    1207, 1249, 1252, 1204 -> if (isNight) drawable.sleet_night else drawable.sleet

    else -> drawable.icon_not_available
}
