package com.example.owner.skymood.model;

import android.content.Context;
import android.util.Log;

import com.example.owner.skymood.model.DAO.SearchedLocationsDAO;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocationManager {
    private static SearchedLocationManager ourInstance;
    SearchedLocationsDAO locationsDAO;

    public static SearchedLocationManager getInstance(Context context) {

        if (ourInstance == null)
            ourInstance = new SearchedLocationManager(context);
        return ourInstance;
    }

    private SearchedLocationManager(Context context) {
        locationsDAO = SearchedLocationsDAO.getInstance(context);
    }

    public ArrayList<SearchedLocation> getAllSearchedLocations(){
        return locationsDAO.getAllSearchedLocations();
    }
    public long insertSearchedLocation(SearchedLocation location){
        return locationsDAO.insertSearchedLocation(location);
    }
}
