package com.example.owner.skymood.model;

import android.content.Context;

import com.example.owner.skymood.model.DAO.MyLocationDAO;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public class MyLocationManager {
    private static MyLocationManager ourInstance;
    private MyLocationDAO locationDAO;

    private MyLocationManager(Context context) {
        this.locationDAO = MyLocationDAO.getInstance(context);
    }

    public static MyLocationManager getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new MyLocationManager(context);
        return ourInstance;
    }

    public ArrayList<MyLocation> getAllMyLocations(){
        return locationDAO.getAllMyLocations();
    }

    public long insertMyLocation(MyLocation location){
        return locationDAO.insertMyLocation(location);
    }

    public MyLocation selectMyLocation(MyLocation location) {
        return locationDAO.selectMyLocation(location);
    }

    public long deleteMyLocation(MyLocation location){
        return locationDAO.deleteMyLocation(location);
    }

    public ArrayList<String> getAllStringLocations(){
        return locationDAO.getAllStringLocations();
    }

    public String selectCuntryCode(String city, String country){
        return locationDAO.selectCuntryCode(city, country);
    }

}
