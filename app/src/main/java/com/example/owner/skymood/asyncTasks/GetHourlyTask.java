package com.example.owner.skymood.asyncTasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.fragments.HourlyWeatherFragment;
import com.example.owner.skymood.model.HourlyWeather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by owner on 09/04/2016.
 */
public class GetHourlyTask extends AsyncTask<String, Void, Void> {

    private final static String API_KEY = "9d48021d05e97609";
    private Context context;
    private SwipeViewActivity activity;
    private HourlyWeatherFragment fragment;
    private ArrayList<HourlyWeather> hourlyWeather;

    public GetHourlyTask(Context context, Fragment fragment, ArrayList<HourlyWeather> hourlyWeather) {
        this.context = context;
        this.fragment = (HourlyWeatherFragment)fragment;
        activity = (SwipeViewActivity)context;
        this.hourlyWeather = hourlyWeather;
    }

    protected Void doInBackground(String... params) {
        try {

            String city = params[0];
            String code = params[1];

            URL url = new URL("http://api.wunderground.com/api/"+API_KEY+"/hourly/q/"+code+"/"+city+".json");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            Scanner sc = new Scanner(connection.getInputStream());
            StringBuilder body = new StringBuilder();
            while(sc.hasNextLine()){
                body.append(sc.nextLine());
            }
            String info = body.toString();
            JSONObject jsonData = new JSONObject(info);
            JSONArray hourlyArray = (JSONArray) jsonData.get("hourly_forecast");


            hourlyWeather = fragment.getHourlyWeatherArray();
            if(hourlyWeather == null){
                Thread.sleep(1000);
                hourlyWeather = fragment.getHourlyWeatherArray();
            }
            hourlyWeather.removeAll(hourlyWeather);

            for(int i = 0; i < hourlyArray.length(); i++){
                JSONObject obj = hourlyArray.getJSONObject(i);
                String hour = obj.getJSONObject("FCTTIME").getString("hour");
                String condition = obj.getString("condition");
                String temp = obj.getJSONObject("temp").getString("metric");
                String icon = obj.getString("icon");

                Integer hourInt = Integer.parseInt(hour);
                int id = 0;
                if (hourInt >= 6 && hourInt <= 19) {
                    id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                } else {
                    icon = icon + "_night";
                    id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
                }

                Bitmap iconImage = BitmapFactory.decodeResource(context.getResources(), id);

                hourlyWeather.add(new HourlyWeather(hour, condition, temp, iconImage));
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
       fragment.getAdapter().notifyDataSetChanged();

    }
}
