package com.example.owner.skymood.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.SwipeViewActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class MoreInfoFragment extends Fragment  implements Swideable{

    private Context context;
    private String city;
    private String code;


    private TextView day;
    private TextView date;
    private TextView temp;
    private TextView feels;
    private TextView min;
    private TextView max;
    private TextView uv;
    private TextView humidity;
    private TextView preassure;
    private TextView visibility;
    private TextView sunrise;
    private TextView sunset;
    private TextView moonAge;
    private TextView moonIlluminated;
    private TextView windSpeed;
    private TextView condition;
    private TextView moonPhase;
    private ProgressBar progress;
    private LinearLayout layout;
    private TextView textCon;

    private String dayTxt;
    private String dateTxt;
    private String tempTxt;
    private String feelsTxt;
    private String minTxt;
    private String maxTxt;
    private String uvTxt;
    private String  humidityTxt;
    private String  preassureTxt;
    private String windsSpeedtxt;
    private String visibilityTxt;
    private String sunriseTxt;
    private String sunsetTxt;
    private String conditionTxt;
    private String moonPhaseTxt;
    private int moonAgeTxt;
    private int moonIllumitatedTxt;
    private static final String API_KEY = "7fc23227bbbc9a36";
    private SwipeViewActivity activity;

    public MoreInfoFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_more_info, container, false);

        this.day = (TextView) root.findViewById(R.id.more_day);
        this.date = (TextView) root.findViewById(R.id.more_date);
        this.temp = (TextView) root.findViewById(R.id.more_temp);
        this.feels = (TextView) root.findViewById(R.id.more_realfeel);
        this.min = (TextView) root.findViewById(R.id.more_min);
        this.max = (TextView) root.findViewById(R.id.more_max);
        this.uv = (TextView) root.findViewById(R.id.more_uv);
        this.preassure = (TextView) root.findViewById(R.id.more_preassure);
        this.humidity = (TextView) root.findViewById(R.id.more_humidity);
        this.visibility = (TextView) root.findViewById(R.id.more_visibility);
        this.sunrise = (TextView) root.findViewById(R.id.more_sunrize);
        this.sunset = (TextView) root.findViewById(R.id.more_sunset);
        this.moonIlluminated = (TextView) root.findViewById(R.id.more_moonIllumitated);
        this.moonAge = (TextView) root.findViewById(R.id.more_moonAge);
        this.condition = (TextView) root.findViewById(R.id.more_condition);
        this.windSpeed = (TextView) root.findViewById(R.id.more_windsSpeed);
        this.moonPhase = (TextView)root.findViewById(R.id.more_phase_of_moon);
        this.progress = (ProgressBar) root.findViewById(R.id.more_progress);
        this.layout = (LinearLayout) root.findViewById(R.id.more_layout);
        this.textCon = (TextView) root.findViewById(R.id.text_condition);

        //root.findViewById(R.id.more_no_internet).setVisibility(View.GONE);
            //new GetMoreInfoTask().execute();
        activity = (SwipeViewActivity) context;
        return root;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    public void setData(){
        this.temp.setText(tempTxt + "℃");
        this.feels.setText(feelsTxt + "℃");
        this.min.setText(minTxt + "℃");
        this.max.setText(maxTxt + "℃");
        this.uv.setText(uvTxt);
        this.humidity.setText(humidityTxt);
        this.preassure.setText(preassureTxt + " hPa");
        this.visibility.setText(visibilityTxt+" km");
        this.sunrise.setText(sunriseTxt);
        this.sunset.setText(sunsetTxt);
        this.moonAge.setText(moonAgeTxt+"");
        this.moonIlluminated.setText(moonIllumitatedTxt+" % illuminated");
        this.windSpeed.setText(windsSpeedtxt + " kmh");
        this.condition.setText(conditionTxt);
        this.date.setText(dateTxt);
        this.day.setText(city);
        this.moonPhase.setText(moonPhaseTxt);
        if(this.condition.getHeight() > textCon.getHeight())
            textCon.setHeight(this.condition.getHeight());
    }

    public void setExternalInfo(String city, String code, String date, String min, String max){
        this.city = city;
        this.code = code;
        this.minTxt = min;
        this.maxTxt = max;
        this.dateTxt = date;
    }

    public void setTaskInfo(String day, String temp, String feels, String  uv, String humidity, String preassure, String windsSpeed, String  visibility, String sunrise, String  sunset, String  condition, String  moonPhase, int moonAge, int illuminate){

        this.dayTxt = day;
        this.tempTxt = temp;
        this.feelsTxt = feels;
        this.uvTxt = uv;
        this.humidityTxt = humidity;
        this.preassureTxt = preassure;
        this.windsSpeedtxt = windsSpeed;
        this.visibilityTxt = visibility;
        this.sunriseTxt = sunrise;
        this.sunsetTxt = sunset;
        this.conditionTxt = condition;
        this.moonPhaseTxt = moonPhase;
        this.moonAgeTxt = moonAge;
        this.moonIllumitatedTxt = illuminate;


    }

    public ProgressBar getProgress() {
        return progress;
    }

    public LinearLayout getLayout() {
        return layout;
    }
}
