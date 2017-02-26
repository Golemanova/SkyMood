package com.example.owner.skymood.asyncTasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.owner.skymood.fragments.CurrentWeatherFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

/**
 * Created by Golemanovaa on 7.4.2016 г..
 */
public class AutoCompleteStringFillerAsyncTask extends AsyncTask<String, Void, Void> {

    private HashMap<String, String> cities;
    private Context context;
    private ArrayList<String> autoCompleteNames;
    private CurrentWeatherFragment fragment;

    public AutoCompleteStringFillerAsyncTask(CurrentWeatherFragment fragment, Context context){
        this.context = context;
        this.fragment = fragment;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            URL url = new URL("http://autocomplete.wunderground.com/aq?query=" + params[0]);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.connect();

            Scanner sc = new Scanner(con.getInputStream());
            StringBuilder body = new StringBuilder();
            while(sc.hasNextLine()){
                body.append(sc.nextLine());
            }
            String info = body.toString();

            JSONObject jsonObj = new JSONObject(info);
            JSONArray results = jsonObj.getJSONArray("RESULTS");
            this.cities = new HashMap<>();
            for(int i = 0; i < results.length(); i++){
                JSONObject location = (JSONObject) results.get(i);
                String name = location.getString("name");
                String country = location.getString("c");
                cities.put(name, country);
            }

            autoCompleteNames = new ArrayList<>();
            for(String name : cities.keySet()){
                autoCompleteNames.add(name);
            }

            fragment.setCities(cities);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        ArrayAdapter adapterAutoComplete = new ArrayAdapter(context, android.R.layout.simple_list_item_1, autoCompleteNames);
        fragment.autoCompleteStringFillerAsyncTaskOnPostExecute(adapterAutoComplete);
    }

}
