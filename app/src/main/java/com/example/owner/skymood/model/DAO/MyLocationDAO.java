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
    private Context context;
    private DatabaseHelper helper;

    private MyLocationDAO(Context context){
        this.context = context;
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

        String[] columns = new String[] {helper.LOCATION_ID, helper.CITY, helper.COUNTRY, helper.COUNTRY_CODE, helper.LOCATION};
        Cursor c = db.query(helper.MY_LOCATIONS, columns, null, null, null, null, null);
        ArrayList<MyLocation> cities = new ArrayList<MyLocation>();
        if(c.moveToFirst()) {
            do {
                long id = c.getLong(c.getColumnIndex(helper.LOCATION_ID));
                String city = c.getString(c.getColumnIndex(helper.CITY));
                String code = c.getString(c.getColumnIndex(helper.COUNTRY_CODE));
                String country = c.getString(c.getColumnIndex(helper.COUNTRY));
                String location = c.getString(c.getColumnIndex(helper.LOCATION));
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
        values.put(helper.CITY, location.getCity());
        values.put(helper.COUNTRY, location.getCountry());
        values.put(helper.COUNTRY_CODE, location.getCode());
        values.put(helper.LOCATION, location.getLocation());
        long id = -1;

        if(selectMyLocation(location) == null)
            id = db.insert(helper.MY_LOCATIONS, null, values);

        db.close();
        return id;
    }

    @Override
    public MyLocation selectMyLocation(MyLocation location) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = new String[] {helper.LOCATION_ID, helper.CITY, helper.COUNTRY, helper.COUNTRY_CODE, helper.LOCATION};
        String selection = helper.CITY + " = ? AND " + helper.COUNTRY + " = ?";
        Cursor c = db.query(helper.MY_LOCATIONS, columns, selection, new String[]{location.getCity(), location.getCountry()}, null, null, null);

        if(c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(helper.LOCATION_ID));
            String city = c.getString(c.getColumnIndex(helper.CITY));
            String code = c.getString(c.getColumnIndex(helper.COUNTRY_CODE));
            String country = c.getString(c.getColumnIndex(helper.COUNTRY));
            String loc = c.getString(c.getColumnIndex(helper.LOCATION));

            return new MyLocation(id, city, code, country, loc);
        }
        else{
            return null;
        }
    }

    @Override
    public long deleteMyLocation(MyLocation location) {
        SQLiteDatabase db = helper.getReadableDatabase();
        long id =  db.delete(helper.MY_LOCATIONS, helper.CITY + " = ? AND " + helper.COUNTRY + " = ?",
                new String[] {location.getCity(), location.getCountry()});
        db.close();
        return id;
    }

    @Override
    public String selectCuntryCode(String city, String country) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = helper.CITY + " = ? AND " + helper.COUNTRY + " = ?";
        Cursor c = db.query(helper.MY_LOCATIONS, new String[]{helper.COUNTRY_CODE}, selection, new String[]{city, country}, null, null, null);
        if(c.moveToFirst()){
            String s = c.getString(c.getColumnIndex(helper.COUNTRY_CODE));
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
        Cursor c = db.query(helper.MY_LOCATIONS,new String[]{ helper.LOCATION}, null, null, null, null, null);
        ArrayList<String> locations = new ArrayList<>();
        if(c.moveToFirst()){
            do{
                locations.add(c.getString(c.getColumnIndex(helper.LOCATION)));
            }
            while(c.moveToNext());
        }
        c.close();
        db.close();
        return locations;
    }


}
