package com.example.owner.skymood.model.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.owner.skymood.model.DatabaseHelper;
import com.example.owner.skymood.model.SearchedLocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by owner on 05/04/2016.
 */
public class SearchedLocationsDAO implements ISearchedLocations {
    private static SearchedLocationsDAO ourInstance;
    DatabaseHelper helper;

    private SearchedLocationsDAO(Context context) {

        helper = DatabaseHelper.getInstance(context);
    }

    public static SearchedLocationsDAO getInstance(Context context) {

        if (ourInstance == null)
            ourInstance = new SearchedLocationsDAO(context);
        return ourInstance;
    }


    @Override
    public ArrayList<SearchedLocation> getAllSearchedLocations() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String[] columns = new String[]{DatabaseHelper.SEARCHED_ID, DatabaseHelper.CITY, DatabaseHelper.TEMP, DatabaseHelper.CONDITION, DatabaseHelper.DATE, DatabaseHelper.COUNTRY,
                DatabaseHelper.COUNTRY_CODE, DatabaseHelper.MAX_TEMP, DatabaseHelper.MIN_TEMP, DatabaseHelper.LAST_UPDATE, DatabaseHelper.ICON, DatabaseHelper.FEELS_LIKE};
        Cursor c = db.query(DatabaseHelper.LAST_SEARCHED, columns, null, null, null, null, null);
        ArrayList<SearchedLocation> cities = new ArrayList<>();
        if (c.moveToFirst())
            do {
                long id = c.getLong(c.getColumnIndex(DatabaseHelper.SEARCHED_ID));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                String temp = c.getString(c.getColumnIndex(DatabaseHelper.TEMP));
                String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));
                String country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY));
                String code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE));
                String max = c.getString(c.getColumnIndex(DatabaseHelper.MAX_TEMP));
                String min = c.getString(c.getColumnIndex(DatabaseHelper.MIN_TEMP));
                String lastUpdate = c.getString(c.getColumnIndex(DatabaseHelper.LAST_UPDATE));
                String icon = c.getString(c.getColumnIndex(DatabaseHelper.ICON));
                String feelsLike = c.getString(c.getColumnIndex(DatabaseHelper.FEELS_LIKE));

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
        if (id != -1) {
            return updateLocation(id, location);
        } else if (getCount() < 5) {
            return insertLocation(location);
        } else {
            id = selectFirstSearchedCity().getId();
            return updateLocation(id, location);
        }
    }

    @Override
    public SearchedLocation selectFirstSearchedCity() {

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = new String[]{DatabaseHelper.SEARCHED_ID, DatabaseHelper.CITY, DatabaseHelper.TEMP, DatabaseHelper.CONDITION, DatabaseHelper.DATE, DatabaseHelper.COUNTRY,
                DatabaseHelper.COUNTRY_CODE, DatabaseHelper.MAX_TEMP, DatabaseHelper.MIN_TEMP, DatabaseHelper.LAST_UPDATE, DatabaseHelper.ICON, DatabaseHelper.FEELS_LIKE};
        Cursor c = db.query(DatabaseHelper.LAST_SEARCHED, columns, null, null, null, null, "datetime(" + DatabaseHelper.DATE + ")", "1");
        SearchedLocation location = null;
        if (c.moveToFirst())
            do {
                long id = c.getLong(c.getColumnIndex(DatabaseHelper.SEARCHED_ID));
                String city = c.getString(c.getColumnIndex(DatabaseHelper.CITY));
                String temp = c.getString(c.getColumnIndex(DatabaseHelper.TEMP));
                String condition = c.getString(c.getColumnIndex(DatabaseHelper.CONDITION));
                String date = c.getString(c.getColumnIndex(DatabaseHelper.DATE));
                String country = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY));
                String code = c.getString(c.getColumnIndex(DatabaseHelper.COUNTRY_CODE));
                String max = c.getString(c.getColumnIndex(DatabaseHelper.MAX_TEMP));
                String min = c.getString(c.getColumnIndex(DatabaseHelper.MIN_TEMP));
                String lastUpdate = c.getString(c.getColumnIndex(DatabaseHelper.LAST_UPDATE));
                String icon = c.getString(c.getColumnIndex(DatabaseHelper.ICON));
                String feelsLike = c.getString(c.getColumnIndex(DatabaseHelper.FEELS_LIKE));

                location = new SearchedLocation(id, city, temp, condition, date, country, code, max, min, lastUpdate, icon, feelsLike);

            }
            while (c.moveToNext());
        c.close();
        db.close();
        return location;
    }

    public long getCount() {

        SQLiteDatabase db = helper.getReadableDatabase();
        String query = "SELECT COUNT(*) FROM " + DatabaseHelper.LAST_SEARCHED;
        SQLiteStatement statement = db.compileStatement(query);
        long count = statement.simpleQueryForLong();
        db.close();
        return count;
    }

    @Override
    public long checkCity(String city) {

        SQLiteDatabase db = helper.getReadableDatabase();


        String selection = DatabaseHelper.CITY + " = ?";
        Cursor c = db.query(DatabaseHelper.LAST_SEARCHED, new String[]{DatabaseHelper.SEARCHED_ID, DatabaseHelper.CITY}, selection, new String[]{city}, null, null, null);
        if (c.moveToFirst()) {
            long id = c.getLong(c.getColumnIndex(DatabaseHelper.SEARCHED_ID));
            c.close();
            db.close();
            return id;
        } else {
            c.close();
            db.close();
            return -1;
        }
    }

    @Override
    public long insertLocation(SearchedLocation location) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CITY, location.getCity());
        values.put(DatabaseHelper.TEMP, location.getTemp());
        values.put(DatabaseHelper.CONDITION, location.getCondition());
        values.put(DatabaseHelper.COUNTRY, location.getCountry());
        values.put(DatabaseHelper.COUNTRY_CODE, location.getCode());
        values.put(DatabaseHelper.MAX_TEMP, location.getMax());
        values.put(DatabaseHelper.MIN_TEMP, location.getMin());
        values.put(DatabaseHelper.LAST_UPDATE, location.getLastUpdate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        values.put(DatabaseHelper.DATE, strDate);
        values.put(DatabaseHelper.ICON, location.getIcon());
        values.put(DatabaseHelper.FEELS_LIKE, location.getFeelsLike());
        long id = db.insert(DatabaseHelper.LAST_SEARCHED, null, values);

        db.close();
        return id;
    }

    @Override
    public long updateLocation(long id, SearchedLocation location) {

        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.CITY, location.getCity());
        values.put(DatabaseHelper.TEMP, location.getTemp());
        values.put(DatabaseHelper.CONDITION, location.getCondition());
        values.put(DatabaseHelper.COUNTRY, location.getCountry());
        values.put(DatabaseHelper.COUNTRY_CODE, location.getCode());
        values.put(DatabaseHelper.MAX_TEMP, location.getMax());
        values.put(DatabaseHelper.MIN_TEMP, location.getMin());
        values.put(DatabaseHelper.LAST_UPDATE, location.getLastUpdate());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(new Date());
        values.put(DatabaseHelper.DATE, strDate);
        values.put(DatabaseHelper.ICON, location.getIcon());
        values.put(DatabaseHelper.FEELS_LIKE, location.getFeelsLike());
        long result = db.update(DatabaseHelper.LAST_SEARCHED, values, DatabaseHelper.SEARCHED_ID + " = ? ", new String[]{"" + id});

        db.close();
        return result;
    }


}
