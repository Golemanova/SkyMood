package com.example.owner.skymood.model;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.owner.skymood.R;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;

/**
 * Created by Golemanovaa on 10.4.2016 г..
 */
public class MyWidgedProvider extends AppWidgetProvider {

    private static final String ACTION_CLICK = "ACTION_CLICK";
    private static String city;
    private static String country;
    private static String countryCode;
    private String temp;
    private String icon;
    private String condition;
    private int iconId;
    private static Context context;

    private static MyWidgedProvider instance = null;

    public static MyWidgedProvider getInstance(){
        if(instance == null){
            instance = new MyWidgedProvider();
        }
        return instance;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.e("VVV", "on update called");
        LocationPreference pref = LocationPreference.getInstance(context);
        this.city = pref.getCity();
        this.country = pref.getCountry();
        this.countryCode = pref.getCountryCode();
        this.context = context;

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if(netInfo != null && netInfo.isConnectedOrConnecting()){

         for (int i = 0; i < appWidgetIds.length; i++) {
             int widgetId = appWidgetIds[i];
             RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);

                Intent intent = new Intent(context, MyWidgedProvider.WidgedService.class);

                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                PendingIntent pendingIntent = PendingIntent.getService(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // request the AppWidgetManager object to update the app widget
                remoteViews.setOnClickPendingIntent(R.id.syncWidget, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        } else {
            // iterate through all of our widgets (in case the user has placed multiple widgets)
            for (int i = 0; i < appWidgetIds.length; i++) {
                int widgetId = appWidgetIds[i];

                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);
                if(!pref.hasNull()) {
                    remoteViews.setTextViewText(R.id.widget_city, city);
                    remoteViews.setTextViewText(R.id.widget_country, country);
                    this.condition = pref.getCondition();
                    remoteViews.setTextViewText(R.id.condition, this.condition);
                    this.temp = pref.getTemperature();
                    remoteViews.setTextViewText(R.id.degree, this.temp + "℃");

                    Field field = null;
                    try {
                        field = R.drawable.class.getDeclaredField(pref.getIcon());
                        iconId = field.getInt(this);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    remoteViews.setImageViewResource(R.id.icon, iconId);
                } else {
                    remoteViews.setTextViewText(R.id.condition, "No Internet Connection :(");
                }

                //update when the update button is clicked
                Intent intent = new Intent(context, MyWidgedProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                //the widgets that should be updated (all of the app widgets)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // request the AppWidgetManager object to update the app widget
                remoteViews.setOnClickPendingIntent(R.id.syncWidget, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }

    public void setInfo(String city, String country, String countryCode){
        this.city = city;
        this.country = country;
        this.countryCode = countryCode;
    }

    public static class WidgedService extends IntentService {

        public WidgedService() {
            super("WidgedService");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            try {
                URL url = new URL("http://api.wunderground.com/api/" + CurrentWeatherFragment.API_KEY + "/conditions/q/" + countryCode + "/" + city + ".json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                Scanner sc = new Scanner(connection.getInputStream());
                StringBuilder body = new StringBuilder();
                while(sc.hasNextLine()){
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONObject observation = (JSONObject) jsonData.get("current_observation");
                String condition = observation.getString("weather");
                String temp = observation.getString("temp_c");
                String icon = observation.getString("icon");


                Field field = R.drawable.class.getDeclaredField(icon);
                int iconId = field.getInt(this);

                RemoteViews remoteV = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
                remoteV.setTextViewText(R.id.widget_city, city);
                remoteV.setTextViewText(R.id.widget_country, country);
                remoteV.setTextViewText(R.id.condition, condition);
                remoteV.setTextViewText(R.id.degree, temp + "℃");
                remoteV.setImageViewResource(R.id.icon, iconId);


                ComponentName thisWidget = new ComponentName(this, MyWidgedProvider.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

                appWidgetManager.updateAppWidget(thisWidget, remoteV);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
