package com.example.owner.skymood.model;

import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;

import com.example.owner.skymood.R;
import com.example.owner.skymood.fragments.CurrentWeatherFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
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
    private String condition;
    private int iconId;

    private static MyWidgedProvider instance = null;

    public static MyWidgedProvider getInstance() {

        if (instance == null) {
            instance = new MyWidgedProvider();
        }
        return instance;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        LocationPreference pref = LocationPreference.getInstance(context);
        city = pref.getCity();
        country = pref.getCountry();
        countryCode = pref.getCountryCode();

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            for (int widgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);

                Intent intent = new Intent(context, MyWidgedProvider.WidgedService.class);

                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

                PendingIntent pendingIntent = PendingIntent.getService(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                // request the AppWidgetManager object to update the app widget
                remoteViews.setOnClickPendingIntent(R.id.widged_layout_btn_sync, pendingIntent);

                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        } else {
            // iterate through all of our widgets (in case the user has placed multiple widgets)
            for (int widgetId : appWidgetIds) {
                RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                        R.layout.widget_layout);
                if (!pref.hasNull()) {
                    remoteViews.setTextViewText(R.id.widget_layout_tv_city, city);
                    remoteViews.setTextViewText(R.id.widget_layout_tv_country, country);
                    this.condition = pref.getCondition();
                    remoteViews.setTextViewText(R.id.widget_layout_tv_condition, this.condition);
                    this.temp = pref.getTemperature();
                    remoteViews.setTextViewText(R.id.widged_layout_tv_degree, this.temp + "℃");

                    Field field;
                    try {
                        field = R.drawable.class.getDeclaredField(pref.getIcon());
                        iconId = field.getInt(this);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    remoteViews.setImageViewResource(R.id.widget_layout_iv_icon, iconId);
                } else {
                    remoteViews.setTextViewText(R.id.widget_layout_tv_condition, "No Internet Connection :(");
                }

                //update when the update button is clicked
                Intent intent = new Intent(context, MyWidgedProvider.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                //the widgets that should be updated (all of the app widgets)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                        0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                // request the AppWidgetManager object to update the app widget
                remoteViews.setOnClickPendingIntent(R.id.widged_layout_btn_sync, pendingIntent);
                appWidgetManager.updateAppWidget(widgetId, remoteViews);
            }
        }
    }

    public void setInfo(String city, String country, String countryCode) {

        MyWidgedProvider.city = city;
        MyWidgedProvider.country = country;
        MyWidgedProvider.countryCode = countryCode;
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
                while (sc.hasNextLine()) {
                    body.append(sc.nextLine());
                }
                String info = body.toString();

                JSONObject jsonData = new JSONObject(info);
                JSONObject observation = (JSONObject) jsonData.get("current_observation");
                String condition = observation.getString("weather");
                String temp = observation.getString("temp_c");
                String icon = observation.getString("widget_layout_iv_icon");


                Field field = R.drawable.class.getDeclaredField(icon);
                int iconId = field.getInt(this);

                RemoteViews remoteV = new RemoteViews(this.getPackageName(), R.layout.widget_layout);
                remoteV.setTextViewText(R.id.widget_layout_tv_city, city);
                remoteV.setTextViewText(R.id.widget_layout_tv_country, country);
                remoteV.setTextViewText(R.id.widget_layout_tv_condition, condition);
                remoteV.setTextViewText(R.id.widged_layout_tv_degree, temp + "℃");
                remoteV.setImageViewResource(R.id.widget_layout_iv_icon, iconId);


                ComponentName thisWidget = new ComponentName(this, MyWidgedProvider.class);
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

                appWidgetManager.updateAppWidget(thisWidget, remoteV);

            } catch (IOException | JSONException | IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
