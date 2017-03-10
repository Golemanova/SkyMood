package com.example.owner.skymood.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.owner.skymood.model.DatabaseHelper;
import com.example.owner.skymood.model.MyLocation;

import java.util.ArrayList;

/**
 * Created by owner on 05/04/2016.
 */
public class MyLocationDAO implements IMyLocationDAO{

    private static MyLocationDAO instance;
    private DatabaseHelper helper;

    private MyLocationDAO(Context context){
        this.helper = DatabaseHelper.getInstance(context);
    }

    public static MyLocationDAO getInstance(Context context){
        if(instance == null)
            instance = new MyLocationDAO(context);
        return instance;
    }


    @Override
    public ArrayList<MyLocation> getAllMyLocations() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = new String[] {DatabaseHelper.LOCATION_ID, DatabaseHelper.CITY, DatabaseHelper.COUNTRY, DatabaseHelper.COUNTRY_CODE, DatabaseHelper.LOCATION};
        Cursor c = db.query(DatabaseHelper.MY_LOCATIONS, columns, null, null, null, null, null);
        ArrayList<MyLocation> cities = new ArrayList<>();
        if(c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndex(DatabaseHelper.LOCATION_ID));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                String code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE));
                String country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY));
                String location = c.getString(c.getColumnIndex(DatabaseHelper.LOCATION));
                cities.add(new MyLocation(id, city, code, country, location));
            }
            while (c.moveToNext());
        }
        c.close();
        db.close();
        return cities;
    }

    @Override
    public long insertMyLocation(MyLocation location) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CITY, location.getCity());
        values.put(DatabaseHelper.COUNTRY, location.getCountry());
        values.put(DatabaseHelper.COUNTRY_CODE, location.getCode());
        values.put(DatabaseHelper.LOCATION, location.getLocation());
        long id = -1;

        if(selectMyLocation(location) == null)
            id = db.insert(DatabaseHelper.MY_LOCATIONS, null, values);

        db.close();
        return id;
    }

    @Override
    public MyLocation selectMyLocation(MyLocation location) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = new String[] {DatabaseHelper.LOCATION_ID, DatabaseHelper.CITY, DatabaseHelper.COUNTRY, DatabaseHelper.COUNTRY_CODE, DatabaseHelper.LOCATION};
        String selection = DatabaseHelper.CITY + " = ? AND " + DatabaseHelper.COUNTRY + " = ?";
        Cursor c = db.query(DatabaseHelper.MY_LOCATIONS, columns, selection, new String[]{location.getCity(), location.getCountry()}, null, null, null);

        if(c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(DatabaseHelper.LOCATION_ID));
            String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
            String code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE));
            String country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY));
            String loc = c.getString(c.getColumnIndex(DatabaseHelper.LOCATION));

            return new MyLocation(id, city, code, country, loc);
        }
        else{
            return null;
        }
    }

    @Override
    public long deleteMyLocation(MyLocation location) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long id =  db.delete(DatabaseHelper.MY_LOCATIONS, DatabaseHelper.CITY + " = ? AND " + DatabaseHelper.COUNTRY + " = ?",
                new String[] {location.getCity(), location.getCountry()});
        db.close();
        return id;
    }

    @Override
    public String selectCountryCode(String city, String country) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = DatabaseHelper.CITY + " = ? AND " + DatabaseHelper.COUNTRY + " = ?";
        Cursor c = db.query(DatabaseHelper.MY_LOCATIONS, new String[]{DatabaseHelper.COUNTRY_CODE}, selection, new String[]{city, country}, null, null, null);
        if(c.moveToFirst()){
            String s = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE));
            c.close();
            db.close();
            return s;
        }
        else {
            c.close();
            db.close();
            return null;
        }
    }

    @Override
    public ArrayList<String> getAllStringLocations() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DatabaseHelper.MY_LOCATIONS,new String[]{DatabaseHelper.LOCATION}, null, null, null, null, null);
        ArrayList<String> locations = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                locations.add(c.getString(c.getColumnIndex(DatabaseHelper.LOCATION)));
            }
            while(c.moveToNext());
        }
        c.close();
        db.close();
        return locations;
    }


}
