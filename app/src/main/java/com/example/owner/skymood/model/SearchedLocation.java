package com.example.owner.skymood.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocation implements Parcelable{
    private long id;
    private String city;
    private String temp;
    private String condition;
    private String date;
    private String country;
    private String code;
    private String max;
    private String min;
    private String lastUpdate;
    private String icon;
    private String feelsLike;


    public SearchedLocation(String city, String temp, String condition, String country, String code, String max, String min, String lastUpdate, String icon, String feelsLike) {
        this.city = city;
        this.temp = temp;
        this.condition = condition;
        this.country = country;
        this.code = code;
        this.max = max;
        this.min = min;
        this.lastUpdate = lastUpdate;
        this.icon = icon;
        this.feelsLike = feelsLike;
    }

    public SearchedLocation( String city, String temp, String condition, String country, String code, String max, String min, String lastUpdate, String icon, String feelsLike, String date) {
        this(city, temp, condition, country, code, max, min, lastUpdate, icon, feelsLike);
        this.date = date;
    }

    public SearchedLocation(long id, String city, String temp, String condition, String country, String code, String max, String min, String lastUpdate, String icon, String feelsLike, String date) {
        this(city, temp, condition, country, code, max, min, lastUpdate, icon, feelsLike, date);
        this.id = id;
    }

    public SearchedLocation(Parcel p){
        this.city = p.readString();
        this.country = p.readString();
        this.condition = p.readString();
        this.icon = p.readString();
        this.temp = p.readString();
        this.min = p.readString();
        this.max = p.readString();
        this.feelsLike = p.readString();
        this.lastUpdate = p.readString();
    }

    public long getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public String getTemp() {
        return temp;
    }

    public String getCondition() {
        return condition;
    }

    public String getDate() {
        return date;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountry() {
        return country;
    }

    public String getCode() {
        return code;
    }

    public String getMax() {
        return max;
    }

    public String getMin() {
        return min;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public String getIcon() {
        return icon;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(condition);
        dest.writeString(icon);
        dest.writeString(temp);
        dest.writeString(min);
        dest.writeString(max);
        dest.writeString(feelsLike);
        dest.writeString(lastUpdate);


    }

    public static final Parcelable.Creator<SearchedLocation> CREATOR = new Parcelable.Creator<SearchedLocation>() {

        @Override
        public SearchedLocation createFromParcel(Parcel source) {
            return new SearchedLocation(source);
        }

        @Override
        public SearchedLocation[] newArray(int size) {
            return new SearchedLocation[size];
        }
    };
}
