package com.example.owner.skymood.asyncTasks

import android.content.Context
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.owner.skymood.fragments.HourlyWeatherFragment
import com.example.owner.skymood.model.HourlyWeather
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

/**
 * Created by owner on 09/04/2016.
 */
class GetHourlyTask(
    private val context: Context,
    fragment: Fragment?,
    private var hourlyWeather: ArrayList<HourlyWeather>
) : AsyncTask<String?, Void?, Void?>() {
    private val fragment: HourlyWeatherFragment = fragment as HourlyWeatherFragment

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): Void? {
        try {
            val city: String? = params[0]
            val code: String? = params[1]

            val url =
                URL("http://api.wunderground.com/api/$API_KEY/hourly/q/$code/$city.json")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()

            val sc = Scanner(connection.getInputStream())
            val body = StringBuilder()
            while (sc.hasNextLine()) {
                body.append(sc.nextLine())
            }
            val info = body.toString()
            Log.d("RESPONSE", "GetHourlyTask response: $info")

            val jsonData = JSONObject(info)
            val hourlyArray = jsonData.get("hourly_forecast") as JSONArray


            hourlyWeather = fragment.hourlyWeatherArray
            hourlyWeather.clear()

            for (i in 0..<hourlyArray.length()) {
                val obj = hourlyArray.getJSONObject(i)
                val hour = obj.getJSONObject("FCTTIME").getString("hour")
                val condition = obj.getString("condition")
                val temp = obj.getJSONObject("temp").getString("metric")
                var icon = obj.getString("icon")

                val hourInt = hour.toInt()
                val id: Int
                if (hourInt in 6..19) {
                    id = context.resources.getIdentifier(icon, "drawable", context.packageName)
                } else {
                    icon += "_night"
                    id = context.resources.getIdentifier(icon, "drawable", context.packageName)
                }

                val iconImage = BitmapFactory.decodeResource(context.resources, id)

                hourlyWeather.add(HourlyWeather(hour, condition, temp, iconImage))
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(aVoid: Void?) {
        fragment.adapter?.notifyDataSetChanged()
    }

    companion object {
        private const val API_KEY = "9d48021d05e97609"
    }
}
