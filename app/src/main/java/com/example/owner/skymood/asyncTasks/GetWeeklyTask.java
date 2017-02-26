package com.example.owner.skymood.asyncTasks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.model.HourlyWeather;
import com.example.owner.skymood.model.WeeklyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by owner on 10/04/2016.
 */
public class GetWeeklyTask extends AsyncTask<String, Void, Void> {

    private final static String API_KEY = "9d48021d05e97609";
    private Context context;
    private SwipeViewActivity activity;
    private Fragment fragment;
    private ArrayList<WeeklyWeather> weeklyWeather;

    public GetWeeklyTask(Context context, Fragment fragment, ArrayList<WeeklyWeather> weeklyWeather) {
        this.context = context;
        this.fragment = fragment;
        activity = (SwipeViewActivity)context;
        this.weeklyWeather = weeklyWeather;
    }

    protected Void doInBackground(String... params) {
        try{
            String city = params[0];
            String code = params[1];
            URL url = new URL("http://api.wunderground.com/api/"+API_KEY+"/forecast7day/q/"+code+"/"+city+".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            Scanner sc = new Scanner(connection.getInputStream());
            StringBuilder body = new StringBuilder();
            while(sc.hasNextLine()){
                body.append(sc.nextLine());
            }
            String info = body.toString();
            JSONObject jsonData = new JSONObject(info);
            JSONObject forecast = jsonData.getJSONObject("forecast");
            JSONObject simpleforecast = forecast.getJSONObject("simpleforecast");
            JSONArray forecastdayArray = (JSONArray) simpleforecast.get("forecastday");

            weeklyWeather = ((HourlyWeatherFragment)fragment).getWeeklyWeatherArray();
            if(weeklyWeather == null){
                Thread.sleep(1000);
            }

            weeklyWeather.removeAll(weeklyWeather);
            weeklyWeather.removeAll(weeklyWeather);
            for(int i = 0; i < forecastdayArray.length(); i++) {
                JSONObject obj = forecastdayArray.getJSONObject(i);
                JSONObject date = obj.getJSONObject("date");

                String day = date.getString("weekday");
                JSONObject high = obj.getJSONObject("high");
                String max = high.getString("celsius");
                JSONObject low = obj.getJSONObject("low");
                String min = low.getString("celsius");
                String condition = obj.getString("conditions");
                String icon = obj.getString("icon");

                int id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                Bitmap iconImage = BitmapFactory.decodeResource(context.getResources(), id);

                weeklyWeather.add(new WeeklyWeather(day, min, max, condition, iconImage));
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ((HourlyWeatherFragment)fragment).getWeekAdapter().notifyDataSetChanged();
    }
}
