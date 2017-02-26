package com.example.owner.skymood.model;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Golemanovaa on 5.4.2016 г..
 */
public class LocationPreference {

    private static LocationPreference instance = null;
    private SharedPreferences pref;
    private Context context;
    private SharedPreferences.Editor editor;
    private MyWidgedProvider widged;
    int PRIVATE_MODE = 0;
    private static final String PREFER_NAME = "SkyModePreferences";
    public static final String CITY = "city";
    public static final String COUNTRY = "country";
    public static final String COUNTRY_CODE = "countryCode";
    public static final String ICON = "icon";
    public static final String TEMPERATURE = "temperature";
    public static final String MIN_TEMP = "minTemp";
    public static final String MAX_TEMP = "maxTemp";
    public static final String CONDITION = "condition";
    public static final String FEELS_LIKE = "feelsLike";
    public static final String LAST_UPDATE = "lastUpdate";

    private LocationPreference(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREFER_NAME, PRIVATE_MODE);
        editor = pref.edit();
        this.widged = MyWidgedProvider.getInstance();
    }

    public static LocationPreference getInstance(Context context){
        if(instance == null){
            instance = new LocationPreference(context);
        }
        return instance;
    }

    public void setPreferredLocation(String city, String country, String countryCode, String icon, String temperature,
                                     String minTemp, String maxTemp, String condition, String feelsLike, String lastUpdate){
        editor.putString(CITY, city);
        editor.putString(COUNTRY, country);
        editor.putString(COUNTRY_CODE, countryCode);
        editor.putString(ICON, icon);
        editor.putString(TEMPERATURE, temperature);
        editor.putString(MIN_TEMP, minTemp);
        editor.putString(MAX_TEMP, maxTemp);
        editor.putString(CONDITION, condition);
        editor.putString(FEELS_LIKE, feelsLike);
        editor.putString(LAST_UPDATE, lastUpdate);
        editor.commit();
        widged.setInfo(city, country, countryCode);
    }

    public boolean isSetLocation(){
        return pref.contains(CITY);
    }

    public String getCity(){
        return pref.getString(CITY, null);
    }

    public String getCountry() {
        return pref.getString(COUNTRY, null);
    }

    public String getCountryCode() {
        return pref.getString(COUNTRY_CODE, null);
    }

    public String getIcon(){
        return pref.getString(ICON, null);
    }

    public String getTemperature(){
        return pref.getString(TEMPERATURE, null);
    }

    public String getMinTemp(){
        return pref.getString(MIN_TEMP, null);
    }

    public String getMaxTemp(){
        return pref.getString(MAX_TEMP, null);
    }

    public String getCondition(){
        return pref.getString(CONDITION, null);
    }

    public String getFeelsLike(){
        return pref.getString(FEELS_LIKE, null);
    }

    public String getLastUpdate(){
        return pref.getString(LAST_UPDATE, null);
    }

    public boolean hasNull(){
        if(getCity() == null || getCountry() == null || getCountryCode() == null || getIcon() == null
                || getTemperature() == null || getMinTemp() == null || getMaxTemp() == null
                || getCondition() == null || getFeelsLike() == null || getLastUpdate() == null){
            return true;
        } else {
            return false;
        }
    }

    public void removeInfo(){
        editor.clear();
        editor.commit();
    }

}
