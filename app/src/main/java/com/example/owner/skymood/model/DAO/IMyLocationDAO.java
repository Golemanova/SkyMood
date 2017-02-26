package com.example.owner.skymood.model.DAO;

import com.example.owner.skymood.model.MyLocation;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public interface IMyLocationDAO {

    ArrayList<MyLocation> getAllMyLocations();
    long insertMyLocation(MyLocation location);
    MyLocation selectMyLocation(MyLocation location);
    long deleteMyLocation(MyLocation location);
    String selectCuntryCode(String city, String country);
    ArrayList<String> getAllStringLocations();
}
