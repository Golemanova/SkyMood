package com.example.owner.skymood.asyncTasks

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.R
import com.example.owner.skymood.R.drawable
import com.example.owner.skymood.fragments.CurrentWeatherFragment
import com.example.owner.skymood.model.LocationPreference
import com.example.owner.skymood.model.SearchedLocation
import com.example.owner.skymood.model.SearchedLocationManager
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Scanner
import kotlin.math.roundToInt

/**
 * Created by Golemanovaa on 7.4.2016 г..
 */
class APIDataGetterAsyncTask(
    private val fragment: Fragment,
    private val context: Context,
    private val weatherImage: ImageView
) : AsyncTask<String?, Void?, Void?>() {
    private var condition: String? = null
    private var icon: String? = null
    private var isNight = false
    private var temp: String? = null
    private var feelsLike: String? = null
    private var maxTemp: String? = null
    private var minTemp: String? = null
    private var dateAndTime: String? = null

    private val locPref: LocationPreference = LocationPreference.getInstance(context)
    private var city: String? = null
    private var countryCode: String? = null
    private var country: String? = null

    var manager: SearchedLocationManager = SearchedLocationManager.getInstance(context)
    var activity: MainActivity = context as MainActivity

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): Void? {
        countryCode = params[0]
        city = params[1]
        country = params[2]

        try {
            //API 1
            val url =
                URL("https://api.weatherapi.com/v1" + "/current.json" + "?q=" + city + "&key=" + CurrentWeatherFragment.API_KEY)

            Log.d("REQUEST", "APIDataGetter request: $url")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val sc = Scanner(connection.getInputStream())
            val body = StringBuilder()
            while (sc.hasNextLine()) {
                body.append(sc.nextLine())
            }
            val info = body.toString()
            Log.d("RESPONSE", "APIDataGetter response: $info")

            val jsonData = JSONObject(info)
            val location = jsonData.get("location") as JSONObject
            city = location.getString("name")
            country = location.getString("country")

            val currentWeather = jsonData.get("current") as JSONObject
            val weatherCondition = currentWeather.get("condition") as JSONObject

            condition = weatherCondition.getString("text")
            val currentTemp = currentWeather.getDouble("temp_c")
            temp = currentTemp.roundToInt().toString()

            val feelsLikeTemp = currentWeather.getDouble("feelslike_c")
            feelsLike = "Feels like: " + feelsLikeTemp.roundToInt() + "℃"
            icon = weatherCondition.getInt("code").toString()
            isNight = weatherCondition.getString("icon").contains("night")

            maxTemp = "10"
            minTemp = "-3"
            country = ""
            countryCode = ""
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        (fragment as CurrentWeatherFragment).apiDataGetterAsyncTaskOnPreExecute()
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(aVoid: Void?) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, 0)
        val format = SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault())
        dateAndTime = format.format(cal.getTime())
        val lastUpdate = "Last update: $dateAndTime"

        if (icon == null) {
            weatherImage.setImageResource(drawable.icon_not_available)
        } else {
            weatherImage.setImageResource(getImageResource(icon!!.toInt(), isNight))
        }

        if (isNight) {
            (context as MainActivity).changeBackground(MainActivity.NIGHT)
        } else {
            (context as MainActivity).changeBackground(MainActivity.DAY)
        }
        activity.setInfo(city, countryCode, minTemp, maxTemp, dateAndTime)
        if (temp != null && icon != null) {
            if (locPref.isSetLocation && city == locPref.city && countryCode == locPref.countryCode) {
                //insert in shared prefs
                locPref.setPreferredLocation(
                    city = city,
                    country = country,
                    countryCode = countryCode,
                    icon = icon,
                    temperature = temp,
                    minTemp = minTemp,
                    maxTemp = maxTemp,
                    condition = condition,
                    feelsLike = feelsLike,
                    lastUpdate = lastUpdate
                )
            } else {
                //insert into DB
                val loc = SearchedLocation(
                    city = city,
                    temp = temp,
                    condition = condition,
                    country = country,
                    code = countryCode,
                    max = maxTemp,
                    min = minTemp,
                    lastUpdate = lastUpdate,
                    icon = icon,
                    feelsLike = feelsLike
                )
                manager.insertSearchedLocation(loc)
            }
        }

        (fragment as CurrentWeatherFragment).apiDataGetterAsyncTaskOnPostExecute(
            temp = temp,
            condition = condition,
            feelsLike = feelsLike,
            minTemp = minTemp,
            maxTemp = maxTemp,
            dateAndTime = dateAndTime,
            lastUpdate = lastUpdate,
            cityToDisplay = city,
            country = country
        )
    }

    private fun getImageResource(code: Int, isNight: Boolean) = when (code) {
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
}
