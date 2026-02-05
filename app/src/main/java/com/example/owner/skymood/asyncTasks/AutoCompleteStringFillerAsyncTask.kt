package com.example.owner.skymood.asyncTasks

import android.R
import android.content.Context
import android.os.AsyncTask
import android.util.Log
import android.widget.ArrayAdapter
import com.example.owner.skymood.fragments.CurrentWeatherFragment
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

/**
 * Created by Golemanovaa on 7.4.2016 г..
 */
class AutoCompleteStringFillerAsyncTask(
    private val fragment: CurrentWeatherFragment,
    private val context: Context
) : AsyncTask<String?, Void?, Void?>() {
    private var cities: HashMap<String?, String?> = hashMapOf()
    private var autoCompleteNames: ArrayList<String?> = arrayListOf()

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: String?): Void? {
        try {
            val url = URL("http://autocomplete.wunderground.com/aq?query=" + params[0])
            val con = url.openConnection() as HttpURLConnection
            con.connect()

            val sc = Scanner(con.getInputStream())
            val body = StringBuilder()
            while (sc.hasNextLine()) {
                body.append(sc.nextLine())
            }
            val info = body.toString()
            Log.d("RESPONSE", "AutoCompleteFiller response: $info")

            val jsonObj = JSONObject(info)
            val results = jsonObj.getJSONArray("RESULTS")
            this.cities = HashMap()
            for (i in 0..<results.length()) {
                val location = results.get(i) as JSONObject
                val name = location.getString("name")
                val country = location.getString("c")
                cities[name] = country
            }

            autoCompleteNames = ArrayList()
            for (name in cities.keys) {
                autoCompleteNames.add(name)
            }

            fragment.setCities(cities)
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(aVoid: Void?) {
        val adapterAutoComplete: ArrayAdapter<String> =
            ArrayAdapter(context, R.layout.simple_list_item_1, autoCompleteNames.toList())
        fragment.autoCompleteStringFillerAsyncTaskOnPostExecute(adapterAutoComplete)
    }
}
