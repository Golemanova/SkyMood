package com.example.owner.skymood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.adapters.HourlyAdapter;
import com.example.owner.skymood.adapters.WeeklyAdapter;
import com.example.owner.skymood.model.HourlyWeather;
import com.example.owner.skymood.model.WeeklyWeather;

import java.util.ArrayList;

public class HourlyWeatherFragment extends Fragment implements Slidable {

    private ArrayList<HourlyWeather> hourlyWeather;
    private ArrayList<WeeklyWeather> weatherArray;
    private RecyclerView hourlyRecycler;
    private RecyclerView weeklyRecycler;
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
            hourlyRecycler = (RecyclerView) view.findViewById(R.id.fragment_hourly_weather_rv_recycler_hourly);
            hourlyRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            HourlyAdapter adapter = new HourlyAdapter(context, hourlyWeather);
            hourlyRecycler.setAdapter(adapter);

            //weekly recycler
            weeklyRecycler = (RecyclerView) view.findViewById(R.id.fragment_hourly_weather_rv_recycler_weekly);
            weeklyRecycler.setLayoutManager(new LinearLayoutManager(context));
            WeeklyAdapter weeklyAdapter = new WeeklyAdapter(weatherArray, context);
            weeklyRecycler.setAdapter(weeklyAdapter);


        return view;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public HourlyAdapter getAdapter(){
        return (HourlyAdapter)this.hourlyRecycler.getAdapter();
    }

    public WeeklyAdapter getWeekAdapter(){
        return (WeeklyAdapter)this.weeklyRecycler.getAdapter();
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
