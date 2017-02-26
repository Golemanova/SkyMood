package com.example.owner.skymood.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.example.owner.skymood.model.DatabaseHelper;
import com.example.owner.skymood.model.SearchedLocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocationsDAO implements ISearchedLocations{
    private static SearchedLocationsDAO ourInstance;
    DatabaseHelper helper;

    private SearchedLocationsDAO(Context context) {
        helper = DatabaseHelper.getInstance(context);
    }

    public static SearchedLocationsDAO getInstance(Context context) {
        if(ourInstance == null)
            ourInstance = new SearchedLocationsDAO(context);
        return ourInstance;
    }


    @Override
    public ArrayList<SearchedLocation> getAllSearchedLocations() {
        SQLiteDatabase db = helper.getReadableDatabase();
        String[] colums = new String[]{helper.SEARCHED_ID, helper.CITY, helper.TEMP, helper.CONDITION, helper.DATE, helper.COUNTRY,
                helper.COUNTRY_CODE, helper.MAX_TEMP, helper.MIN_TEMP, helper.LAST_UPDATE, helper.ICON, helper.FEELS_LIKE};
        Cursor c = db.query(helper.LAST_SEARCHED, colums, null, null, null, null, null);
        ArrayList<SearchedLocation> cities = new ArrayList<SearchedLocation>();
        if(c.moveToFirst())
            do{
                long id = c.getLong(c.getColumnIndex(helper.SEARCHED_ID));
                String city = c.getString(c.getColumnIndex(helper.CITY));
                String temp = c.getString(c.getColumnIndex(helper.TEMP));
                String condition = c.getString(c.getColumnIndex(helper.CONDITION));
                String date = c.getString(c.getColumnIndex(helper.DATE));
                String country = c.getString(c.getColumnIndex(helper.COUNTRY));
                String code = c.getString(c.getColumnIndex(helper.COUNTRY_CODE));
                String max = c.getString(c.getColumnIndex(helper.MAX_TEMP));
                String min = c.getString(c.getColumnIndex(helper.MIN_TEMP));
                String lastUpdate = c.getString(c.getColumnIndex(helper.LAST_UPDATE));
                String icon = c.getString(c.getColumnIndex(helper.ICON));
                String feelsLike = c.getString(c.getColumnIndex(helper.FEELS_LIKE));

                SearchedLocation location = new SearchedLocation(id, city, temp, condition, country, code, max, min, lastUpdate, icon, feelsLike, date);
                cities.add(location);
            }
            while (c.moveToNext());
        c.close();
        db.close();
        return cities;
    }

    @Override
    public long insertSearchedLocation(SearchedLocation location) {
        long id = checkCity(location.getCity());
        if (id != -1){
            return updateLocation(id, location);
        }
        else if(getCount() < 5){
            return insertLocation(location);
        }
        else{
            id = selectFirstSearchedCity().getId();
            return updateLocation(id, location);
        }
    }

    @Override
    public SearchedLocation selectFirstSearchedCity() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] colums = new String[]{helper.SEARCHED_ID, helper.CITY, helper.TEMP, helper.CONDITION, helper.DATE, helper.COUNTRY,
        helper.COUNTRY_CODE, helper.MAX_TEMP, helper.MIN_TEMP, helper.LAST_UPDATE, helper.ICON, helper.FEELS_LIKE};
        Cursor c = db.query(helper.LAST_SEARCHED, colums, null, null, null, null, "datetime("+ helper.DATE +")", "1");
        SearchedLocation location = null;
        if(c.moveToFirst())
            do{
                long id = c.getLong(c.getColumnIndex(helper.SEARCHED_ID));
                String city = c.getString(c.getColumnIndex(helper.CITY));
                String temp = c.getString(c.getColumnIndex(helper.TEMP));
                String condition = c.getString(c.getColumnIndex(helper.CONDITION));
                String date = c.getString(c.getColumnIndex(helper.DATE));
                String country = c.getString(c.getColumnIndex(helper.COUNTRY));
                String code = c.getString(c.getColumnIndex(helper.COUNTRY_CODE));
                String max = c.getString(c.getColumnIndex(helper.MAX_TEMP));
                String min = c.getString(c.getColumnIndex(helper.MIN_TEMP));
                String lastUpdate = c.getString(c.getColumnIndex(helper.LAST_UPDATE));
                String icon = c.getString(c.getColumnIndex(helper.ICON));
                String feelsLike = c.getString(c.getColumnIndex(helper.FEELS_LIKE));

                location = new SearchedLocation(id, city, temp, condition, date, country, code, max, min, lastUpdate, icon, feelsLike);

            }
            while (c.moveToNext());
        c.close();
        db.close();
        return location;
    }

    public long getCount(){
        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + helper.LAST_SEARCHED;
        SQLiteStatement statement = db.compileStatement(query);
        long count = statement.simpleQueryForLong();
        db.close();
        return count;
    }

    @Override
    public long checkCity(String city) {
        SQLiteDatabase db = helper.getReadableDatabase();


        String selection = helper.CITY + " = ?";
        Cursor c = db.query(helper.LAST_SEARCHED, new String[]{helper.SEARCHED_ID, helper.CITY}, selection, new String[]{city}, null, null, null );
        if(c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(helper.SEARCHED_ID));
            c.close();
            db.close();
            return id;
        }
        else {
            c.close();
            db.close();
            return -1;
        }
    }

    @Override
    public long insertLocation(SearchedLocation location) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.CITY, location.getCity());
        values.put(helper.TEMP, location.getTemp());
        values.put(helper.CONDITION, location.getCondition());
        values.put(helper.COUNTRY, location.getCountry());
        values.put(helper.COUNTRY_CODE, location.getCode());
        values.put(helper.MAX_TEMP, location.getMax());
        values.put(helper.MIN_TEMP, location.getMin());
        values.put(helper.LAST_UPDATE, location.getLastUpdate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        values.put(helper.DATE, strDate);
        values.put(helper.ICON, location.getIcon());
        values.put(helper.FEELS_LIKE, location.getFeelsLike());
        long id = db.insert(helper.LAST_SEARCHED, null, values);

        db.close();
        return id;
    }

    @Override
    public long updateLocation(long id, SearchedLocation location) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(helper.CITY, location.getCity());
        values.put(helper.TEMP, location.getTemp());
        values.put(helper.CONDITION, location.getCondition());
        values.put(helper.COUNTRY, location.getCountry());
        values.put(helper.COUNTRY_CODE, location.getCode());
        values.put(helper.MAX_TEMP, location.getMax());
        values.put(helper.MIN_TEMP, location.getMin());
        values.put(helper.LAST_UPDATE, location.getLastUpdate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        values.put(helper.DATE, strDate);
        values.put(helper.ICON, location.getIcon());
        values.put(helper.FEELS_LIKE, location.getFeelsLike());
        long result = db.update(helper.LAST_SEARCHED, values, helper.SEARCHED_ID + " = ? ", new String[]{""+id});

        db.close();
        return result;
    }


}
