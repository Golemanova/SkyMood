package com.example.owner.skymood.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;
import com.example.owner.skymood.adapters.HourlyAdapter;
import com.example.owner.skymood.adapters.WeeklyAdapter;
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

public class HourlyWeatherFragment extends Fragment implements Swideable{

    private ArrayList<HourlyWeather> hourlyWeather;
    private ArrayList<WeeklyWeather> weatherArray;
    private RecyclerView hourlyRecycler;
    private RecyclerView weerklyRecycler;
    private Context context;
    private LinearLayout layout;
    private TextView text;

    public HourlyWeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.fragment_hourly_weather, container, false);
        this.setRetainInstance(true);

        //layout = (LinearLayout) view.findViewById(R.id.hourlyLayout);
        hourlyWeather = new ArrayList<>();
        weatherArray = new ArrayList<>();

           // text = (TextView) view.findViewById(R.id.hour_no_internet);
            // hourly recycler
            hourlyRecycler = (RecyclerView) view.findViewById(R.id.recycler_hourly);
            hourlyRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            HourlyAdapter adapter = new HourlyAdapter(context, hourlyWeather);
            hourlyRecycler.setAdapter(adapter);

            //weekly recycler
            weerklyRecycler = (RecyclerView) view.findViewById(R.id.recycler_weekly);
            weerklyRecycler.setLayoutManager(new LinearLayoutManager(context));
            WeeklyAdapter weeklyAdapte = new WeeklyAdapter(weatherArray, context);
            weerklyRecycler.setAdapter(weeklyAdapte);


        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HourlyAdapter getAdapter(){
        return (HourlyAdapter)this.hourlyRecycler.getAdapter();
    }

    public WeeklyAdapter getWeekAdapter(){
        return (WeeklyAdapter)this.weerklyRecycler.getAdapter();
    }

    public ArrayList<HourlyWeather> getHourlyWeatherArray() {
        return this.hourlyWeather;
    }

    public ArrayList<WeeklyWeather> getWeeklyWeatherArray() {
        return this.weatherArray;
    }

    public TextView getText() {
        return text;
    }

    public LinearLayout getLayout() {
        return layout;
    }
}
