package com.example.owner.skymood.asyncTasks

import android.content.Context
import android.os.AsyncTask
import android.text.format.DateFormat
import android.util.Log
import android.view.View
import com.example.owner.skymood.fragments.MoreInfoFragment
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Date
import java.util.Scanner

/**
 * Created by owner on 11/04/2016.
 */
class GetMoreInfoTask(
    private val fragment: MoreInfoFragment
) : AsyncTask<String?, Void?, Void?>() {

    private var dayTxt: String? = null
    private var tempTxt: String? = null
    private var feelsTxt: String? = null
    private var uvTxt: String? = null
    private var humidityTxt: String? = null
    private var pressureTxt: String? = null
    private var windsSpeedTxt: String? = null
    private var visibilityTxt: String? = null
    private var sunriseTxt: String? = null
    private var sunsetTxt: String? = null
    private var conditionTxt: String? = null
    private var moonPhaseTxt: String? = null
    private var moonAgeTxt = 0
    private var moonIlluminatedTxt = 0

    @Deprecated("Deprecated in Java")
    override fun onPreExecute() {
        super.onPreExecute()
        fragment.progress.visibility = View.VISIBLE
        fragment.layout.visibility = View.GONE
    }

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): Void? {
        val city: String? = params[0]
        val code: String? = params[1]
        try {
            val url =
                URL("http://api.wunderground.com/api/$API_KEY/conditions/q/$code/$city.json")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val sc = Scanner(connection.getInputStream())
            val body = StringBuilder()
            while (sc.hasNextLine()) {
                body.append(sc.nextLine())
            }
            val info = body.toString()
            Log.d("RESPONSE", "GetMoreInfoTask response: $info")

            val jsonData = JSONObject(info)
            val observation = jsonData.get("current_observation") as JSONObject
            conditionTxt = observation.getString("weather")
            tempTxt = observation.getString("temp_c")
            feelsTxt = observation.getString("feelslike_c")
            uvTxt = observation.getString("UV")
            humidityTxt = observation.getString("relative_humidity")
            windsSpeedTxt = observation.getString("wind_kph")
            visibilityTxt = observation.getString("visibility_km")
            pressureTxt = observation.getString("pressure_mb")
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        try {
            val astronomyUrl =
                URL("http://api.wunderground.com/api/$API_KEY/astronomy/q/$code/$city.json")
            val secondConnection = astronomyUrl.openConnection() as HttpURLConnection
            secondConnection.connect()
            val sc2 = Scanner(secondConnection.getInputStream())
            val bodyBuilder = StringBuilder()
            while (sc2.hasNextLine()) {
                bodyBuilder.append(sc2.nextLine())
            }
            val astronomyJSON = bodyBuilder.toString()


            val jsonData = JSONObject(astronomyJSON)
            val moon = jsonData.get("moon_phase") as JSONObject
            val sun = jsonData.get("sun_phase") as JSONObject
            val sun_rise = sun.getJSONObject("sunrise")
            val sun_set = sun.getJSONObject("sunset")
            moonAgeTxt = moon.getString("ageOfMoon").toInt()
            moonIlluminatedTxt = moon.getString("percentIlluminated").toInt()
            moonPhaseTxt = moon.getString("phaseofMoon")
            sunriseTxt = sun_rise.get("hour").toString() + ":" + sun_rise.get("minute")

            sunsetTxt = sun_set.get("hour").toString() + ":" + sun_set.get("minute")
            val d = Date()
            dayTxt = DateFormat.format("EEEE", d) as String?
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(aVoid: Void?) {
        super.onPostExecute(aVoid)
        fragment.setTaskInfo(
            dayTxt,
            tempTxt,
            feelsTxt,
            uvTxt,
            humidityTxt,
            pressureTxt,
            windsSpeedTxt,
            visibilityTxt,
            sunriseTxt,
            sunsetTxt,
            conditionTxt,
            moonPhaseTxt,
            moonAgeTxt,
            moonIlluminatedTxt
        )
        fragment.setData()
        fragment.progress.visibility = View.GONE
        fragment.layout.visibility = View.VISIBLE
    }

    companion object {
        private const val API_KEY = "7fc23227bbbc9a36"
    }
}
