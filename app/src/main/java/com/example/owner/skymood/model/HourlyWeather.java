package com.example.owner.skymood.model;

import android.graphics.Bitmap;

import java.sql.Time;

/**
 * Created by owner on 04/04/2016.
 */
public class HourlyWeather {

    private String hour;
    private String condition;
    private String temp;
    private Bitmap icon;

    public HourlyWeather(String hour, String condition, String temp, Bitmap icon) {
        this.hour = hour;
        this.condition = condition;
        this.temp = temp;
        this.icon = icon;
    }

    public String getHour() {
        return hour;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public String getCondition() {
        return condition;
    }

    public String getTemp() {
        return temp;
    }
}
