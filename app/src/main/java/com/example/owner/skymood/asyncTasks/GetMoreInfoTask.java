package com.example.owner.skymood.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.owner.skymood.MainActivity;
import com.example.owner.skymood.fragments.MoreInfoFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by owner on 11/04/2016.
 */
public class GetMoreInfoTask extends AsyncTask<String, Void, Void> {

    private final static String API_KEY = "7fc23227bbbc9a36";
    private Context context;
    private MainActivity activity;
    private MoreInfoFragment fragment;

    private String dayTxt;
    private String tempTxt;
    private String feelsTxt;
    private String uvTxt;
    private String  humidityTxt;
    private String pressureTxt;
    private String windsSpeedTxt;
    private String visibilityTxt;
    private String sunriseTxt;
    private String sunsetTxt;
    private String conditionTxt;
    private String moonPhaseTxt;
    private int moonAgeTxt;
    private int moonIlluminatedTxt;

    public GetMoreInfoTask(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = (MoreInfoFragment)fragment;
        activity = (MainActivity)context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fragment.getProgress().setVisibility(View.VISIBLE);
        fragment.getLayout().setVisibility(View.GONE);
    }

    @Override
    protected Void doInBackground(String... params) {
        String city = params[0];
        String code = params[1];
        try {
            URL url = new URL("http://api.wunderground.com/api/" + API_KEY + "/conditions/q/" + code + "/" + city + ".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            Scanner sc = new Scanner(connection.getInputStream());
            StringBuilder body = new StringBuilder();
            while (sc.hasNextLine()) {
                body.append(sc.nextLine());
            }
            String info = body.toString();

            JSONObject jsonData = new JSONObject(info);
            JSONObject observation = (JSONObject) jsonData.get("current_observation");
            conditionTxt = observation.getString("weather");
            tempTxt = observation.getString("temp_c");
            feelsTxt = observation.getString("feelslike_c");
            uvTxt = observation.getString("UV");
            humidityTxt = observation.getString("relative_humidity");
            windsSpeedTxt = observation.getString("wind_kph");
            visibilityTxt = observation.getString("visibility_km");
            pressureTxt = observation.getString("pressure_mb");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        try {
            URL astronomyUrl = new URL("http://api.wunderground.com/api/" + API_KEY + "/astronomy/q/" + code + "/" + city + ".json");
            HttpURLConnection secondConnection = (HttpURLConnection) astronomyUrl.openConnection();
            secondConnection.connect();
            Scanner sc2 = new Scanner(secondConnection.getInputStream());
            StringBuilder bodyBuilder = new StringBuilder();
            while (sc2.hasNextLine()) {
                bodyBuilder.append(sc2.nextLine());
            }
            String astronomyJSON = bodyBuilder.toString();


            JSONObject jsonData = new JSONObject(astronomyJSON);
            JSONObject moon = (JSONObject) jsonData.get("moon_phase");
            JSONObject sun = (JSONObject) jsonData.get("sun_phase");
            JSONObject sun_rise = sun.getJSONObject("sunrise");
            JSONObject sun_set = sun.getJSONObject("sunset");
            moonAgeTxt = Integer.valueOf(moon.getString("ageOfMoon"));
            moonIlluminatedTxt = Integer.valueOf(moon.getString("percentIlluminated"));
            moonPhaseTxt = moon.getString("phaseofMoon");
            sunriseTxt = sun_rise.get("hour") + ":" + sun_rise.get("minute");

            sunsetTxt = sun_set.get("hour") + ":" + sun_set.get("minute");
            Date d = new Date();
            dayTxt = (String) android.text.format.DateFormat.format("EEEE", d);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        fragment.setTaskInfo(dayTxt, tempTxt, feelsTxt, uvTxt, humidityTxt, pressureTxt, windsSpeedTxt, visibilityTxt, sunriseTxt, sunsetTxt, conditionTxt, moonPhaseTxt, moonAgeTxt, moonIlluminatedTxt);
        fragment.setData();
        fragment.getProgress().setVisibility(View.GONE);
        fragment.getLayout().setVisibility(View.VISIBLE);
    }
}
