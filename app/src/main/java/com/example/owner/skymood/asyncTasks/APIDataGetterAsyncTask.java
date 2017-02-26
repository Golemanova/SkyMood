package com.example.owner.skymood.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;
import com.example.owner.skymood.model.LocationPreference;
import com.example.owner.skymood.model.SearchedLocation;
import com.example.owner.skymood.model.SearchedLocationManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Golemanovaa on 7.4.2016 г..
 */
public class APIDataGetterAsyncTask extends AsyncTask<String, Void, Void> {

    private String condition;
    private String icon;
    private String temp;
    private String feelsLike;
    private String maxTemp;
    private String minTemp;
    private String dateAndTime;

    private Fragment fragment;
    private Context context;
    private ImageView weatherImage;
    private LocationPreference locPref;
    private String city;
    private String countryCode;
    private String country;

    SearchedLocationManager manager;
    SwipeViewActivity activity;

    public APIDataGetterAsyncTask(Fragment f, Context context, ImageView weatherImage){
        this.fragment = f;
        this.context = context;
        this.weatherImage = weatherImage;
        this.locPref = LocationPreference.getInstance(context);
        this.manager = SearchedLocationManager.getInstance(context);
        activity = (SwipeViewActivity) context;
    }

    @Override
    protected Void doInBackground(String... params) {
        countryCode = params[0];
        city = params[1];
        country = params[2];

        try {
            //API 1
            URL url = new URL("http://api.wunderground.com/api/" + CurrentWeatherFragment.API_KEY + "/conditions/q/" + countryCode + "/" + city + ".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            Scanner sc = new Scanner(connection.getInputStream());
            StringBuilder body = new StringBuilder();
            while(sc.hasNextLine()){
                body.append(sc.nextLine());
            }
            String info = body.toString();

            JSONObject jsonData = new JSONObject(info);
            JSONObject observation = (JSONObject) jsonData.get("current_observation");
            condition = observation.getString("weather");
            temp = observation.getString("temp_c");
            feelsLike = "Feels like: " + observation.getString("feelslike_c") + "℃";
            icon = observation.getString("icon");

            //API 2
            URL url2 = new URL("http://api.wunderground.com/api/"+ CurrentWeatherFragment.API_KEY_TWO +"/forecast/q/" + countryCode + "/" + city + ".json");
            HttpURLConnection connection2 = (HttpURLConnection) url2.openConnection();
            connection2.connect();

            Scanner sc2 = new Scanner(connection2.getInputStream());
            StringBuilder data = new StringBuilder();
            while(sc2.hasNextLine()){
                data.append(sc2.nextLine());
            }
            String dataJson = data.toString();
            JSONObject dataJsonObj = new JSONObject(dataJson);
            JSONObject forecast = dataJsonObj.getJSONObject("forecast");
            JSONObject simpleForecast = forecast.getJSONObject("simpleforecast");
            JSONArray forecastDay = simpleForecast.getJSONArray("forecastday");
            JSONObject day = (JSONObject) forecastDay.get(0);
            JSONObject high = day.getJSONObject("high");
            maxTemp = high.getString("celsius");
            JSONObject low = day.getJSONObject("low");
            minTemp = low.getString("celsius");

//            if(Double.parseDouble(temp) > Double.parseDouble(maxTemp)){
//                Double max = Math.ceil(Double.parseDouble(temp));
//                Integer maxTempInt = max.intValue();
//                maxTemp = maxTempInt.toString();
//            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        ((CurrentWeatherFragment)fragment).apiDataGetterAsyncTaskOnPreExecute();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, 0);
            SimpleDateFormat format = new SimpleDateFormat("HH:mm, dd.MM.yyyy");
            dateAndTime = format.format(cal.getTime());
            String lastUpdate = "Last update: " + dateAndTime;

            Context con = weatherImage.getContext();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int id = 0;
            if (icon == null) {
                weatherImage.setImageResource(R.drawable.nulll);
            } else {
                if (hour >= 6 && hour <= 19) {
                    id = context.getResources().getIdentifier(icon, "drawable", con.getPackageName());
                    ((SwipeViewActivity)context).changeBackground(SwipeViewActivity.DAY);
                } else {
                    icon = icon + "_night";
                    id = context.getResources().getIdentifier(icon, "drawable", con.getPackageName());
                    ((SwipeViewActivity)context).changeBackground(SwipeViewActivity.NIGHT);
                }
                weatherImage.setImageResource(id);
            }
            activity.setInfo(city, countryCode, minTemp, maxTemp, dateAndTime);
            if(temp != null && icon != null) {
                if (locPref.isSetLocation() && city.equals(locPref.getCity()) && countryCode.equals(locPref.getCountryCode())) {
                    //insert in shared prefs
                    locPref.setPreferredLocation(city, country, countryCode, icon, temp, minTemp, maxTemp, condition, feelsLike, lastUpdate);
                }
                else {
                    //insert into DB
                    SearchedLocation loc = new SearchedLocation(city, temp, condition, country, countryCode, maxTemp, minTemp, lastUpdate, icon, feelsLike);
                    manager.insertSearchedLocation(loc);
                }
            }



        ((CurrentWeatherFragment) fragment).apiDataGetterAsyncTaskOnPostExecute(temp, condition, feelsLike, minTemp, maxTemp, dateAndTime, lastUpdate, city, country);

    }

}
