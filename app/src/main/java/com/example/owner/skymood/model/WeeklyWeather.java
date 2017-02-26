package com.example.owner.skymood.model;

import android.graphics.Bitmap;

/**
 * Created by owner on 05/04/2016.
 */
public class WeeklyWeather {

    private String day;
    private String min;
    private String max;
    private String condition;
    private Bitmap icon;

    public WeeklyWeather(String day, String min, String max, String condition, Bitmap icon) {
        this.day = day;
        this.min = min;
        this.max = max;
        this.condition = condition;
        this.icon = icon;
    }

    public String getDay() {
        return day;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getCondition() {
        return condition;
    }

    public Bitmap getIcon() {
        return icon;
    }
}
