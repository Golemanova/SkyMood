package com.example.owner.skymood.asyncTasks

import android.content.Context
import android.os.AsyncTask
import android.widget.ImageView
import com.example.owner.skymood.MainActivity
import com.example.owner.skymood.fragments.CurrentWeatherFragment

/**
 * Created by Golemanovaa on 8.4.2016 г..
 */
class FindLocationAsyncTask(
    private val fragment: CurrentWeatherFragment,
    private val context: Context,
    private val weatherImage: ImageView
) : AsyncTask<Void?, Void?, Void?>() {
    private var city: String? = null
    private val countryCode: String? = null
    private var country: String? = null

    @Deprecated("Deprecated in Java")
    override fun doInBackground(vararg params: Void?): Void? {
        //        try {
//            URL url = new URL("http://api.wunderground.com/api/" + CurrentWeatherFragment.API_KEY + "/geolookup/q/autoip.json");
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.connect();
//
//            Scanner sc = new Scanner(con.getInputStream());
//            StringBuilder body = new StringBuilder();
//            while (sc.hasNextLine()) {
//                body.append(sc.nextLine());
//            }
//            String info = body.toString();
//            Log.d("RESPONSE", "FindLocation response: " + info);
//
//            JSONObject data = new JSONObject(info);
//            JSONObject location = data.getJSONObject("location");
//            countryCode = location.getString("country_iso3166");
//            country = location.getString("country_name");
//            country = country.trim();
//            city = location.getString("city");


//        } catch (IOException | JSONException e) {
//            e.printStackTrace();
//        }


        city = "Sofia"
        country = "Bulgaria"
        return null
    }

    @Deprecated("Deprecated in Java")
    override fun onPostExecute(aVoid: Void?) {
        fragment.setCity(city, country)
        val task = APIDataGetterAsyncTask(fragment, context, weatherImage)

        //get current weather
        task.execute(countryCode, city, country)
        val fr = (context as MainActivity).hourlyFragment

        //get 24 hours forecast
        val hourTask = GetHourlyTask(context, fr, fr.hourlyWeatherArray)
        hourTask.execute(city, countryCode)

        //get 7 days forecast
        val weeklyTask = GetWeeklyTask(context, fr, fr.weeklyWeatherArray)
        weeklyTask.execute(city, countryCode)
    }
}
