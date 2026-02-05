package com.example.owner.skymood.asyncTasks

import android.content.Context
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.owner.skymood.fragments.HourlyWeatherFragment
import com.example.owner.skymood.model.WeeklyWeather
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

/**
 * Created by owner on 10/04/2016.
 */
class GetWeeklyTask(
    private val context: Context,
    private val fragment: Fragment,
    private var weeklyWeather: ArrayList<WeeklyWeather>
) : AsyncTask<String?, Void?, Void?>() {

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): Void? {
        try {
            val city: String? = params[0]
            val code: String? = params[1]
            val url =
                URL("http://api.wunderground.com/api/$API_KEY/forecast7day/q/$code/$city.json")
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
            val forecast = jsonData.getJSONObject("forecast")
            val simpleForecast = forecast.getJSONObject("simpleforecast")
            val forecastDayArray = simpleForecast.get("forecastday") as JSONArray

            weeklyWeather = (fragment as HourlyWeatherFragment).weeklyWeatherArray

            weeklyWeather.clear()
            for (i in 0..<forecastDayArray.length()) {
                val obj = forecastDayArray.getJSONObject(i)
                val date = obj.getJSONObject("date")

                val day = date.getString("weekday")
                val high = obj.getJSONObject("high")
                val max = high.getString("celsius")
                val low = obj.getJSONObject("low")
                val min = low.getString("celsius")
                val condition = obj.getString("conditions")
                val icon = obj.getString("icon")

                val id =
                    context.getResources().getIdentifier(icon, "drawable", context.getPackageName())
                val iconImage = BitmapFactory.decodeResource(context.getResources(), id)

                weeklyWeather.add(WeeklyWeather(day, min, max, condition, iconImage))
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
        (fragment as HourlyWeatherFragment).weekAdapter?.notifyDataSetChanged()
    }

    companion object {
        private const val API_KEY = "9d48021d05e97609"
    }
}
