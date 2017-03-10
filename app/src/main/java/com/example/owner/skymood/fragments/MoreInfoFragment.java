package com.example.owner.skymood.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.owner.skymood.R;
import com.example.owner.skymood.MainActivity;

public class MoreInfoFragment extends Fragment  implements Slidable {

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
    private TextView pressure;
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
    private String pressureTxt;
    private String windsSpeedTxt;
    private String visibilityTxt;
    private String sunriseTxt;
    private String sunsetTxt;
    private String conditionTxt;
    private String moonPhaseTxt;
    private int moonAgeTxt;
    private int moonIlluminatedTxt;
    private static final String API_KEY = "7fc23227bbbc9a36";
    private MainActivity activity;

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

        this.day = (TextView) root.findViewById(R.id.fragment_more_info_tv_day);
        this.date = (TextView) root.findViewById(R.id.fragment_more_info_tv_date);
        this.temp = (TextView) root.findViewById(R.id.fragment_more_info_tv_temp_value);
        this.feels = (TextView) root.findViewById(R.id.fragment_more_info_tv_real_feel_value);
        this.min = (TextView) root.findViewById(R.id.fragment_more_info_tv_min_value);
        this.max = (TextView) root.findViewById(R.id.fragment_more_info_tv_max_value);
        this.uv = (TextView) root.findViewById(R.id.fragment_more_info_tv_uv_index_value);
        this.pressure = (TextView) root.findViewById(R.id.fragment_more_info_tv_pressure_value);
        this.humidity = (TextView) root.findViewById(R.id.fragment_more_info_tv_humidity_value);
        this.visibility = (TextView) root.findViewById(R.id.fragment_more_info_tv_visibility_value);
        this.sunrise = (TextView) root.findViewById(R.id.fragment_more_info_tv_sunrise_value);
        this.sunset = (TextView) root.findViewById(R.id.fragment_more_info_tv_sunset_value);
        this.moonIlluminated = (TextView) root.findViewById(R.id.fragment_more_info_tv_moon_value);
        this.moonAge = (TextView) root.findViewById(R.id.fragment_more_info_tv_moon_age_value);
        this.condition = (TextView) root.findViewById(R.id.fragment_more_info_tv_condition_value);
        this.windSpeed = (TextView) root.findViewById(R.id.fragment_more_info_tv_winds_speed_value);
        this.moonPhase = (TextView)root.findViewById(R.id.fragment_more_info_tv_phase_of_moon_value);
        this.progress = (ProgressBar) root.findViewById(R.id.fragment_more_info_view_progress_bar);
        this.layout = (LinearLayout) root.findViewById(R.id.fragment_more_info_container);
        this.textCon = (TextView) root.findViewById(R.id.fragment_more_info_tv_condition);

        //root.findViewById(R.id.more_no_internet).setVisibility(View.GONE);
            //new GetMoreInfoTask().execute();
        activity = (MainActivity) context;
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
        this.pressure.setText(pressureTxt + " hPa");
        this.visibility.setText(visibilityTxt+" km");
        this.sunrise.setText(sunriseTxt);
        this.sunset.setText(sunsetTxt);
        this.moonAge.setText(moonAgeTxt+"");
        this.moonIlluminated.setText(moonIlluminatedTxt +" % illuminated");
        this.windSpeed.setText(windsSpeedTxt + " kmh");
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

    public void setTaskInfo(String day, String temp, String feels, String  uv, String humidity, String pressure, String windsSpeed, String  visibility, String sunrise, String  sunset, String  condition, String  moonPhase, int moonAge, int illuminate){

        this.dayTxt = day;
        this.tempTxt = temp;
        this.feelsTxt = feels;
        this.uvTxt = uv;
        this.humidityTxt = humidity;
        this.pressureTxt = pressure;
        this.windsSpeedTxt = windsSpeed;
        this.visibilityTxt = visibility;
        this.sunriseTxt = sunrise;
        this.sunsetTxt = sunset;
        this.conditionTxt = condition;
        this.moonPhaseTxt = moonPhase;
        this.moonAgeTxt = moonAge;
        this.moonIlluminatedTxt = illuminate;


    }

    public ProgressBar getProgress() {
        return progress;
    }

    public LinearLayout getLayout() {
        return layout;
    }
}
