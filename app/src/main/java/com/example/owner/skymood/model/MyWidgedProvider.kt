package com.example.owner.skymood.model

import android.app.IntentService
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import com.example.owner.skymood.R
import com.example.owner.skymood.R.drawable
import com.example.owner.skymood.fragments.CurrentWeatherFragment
import org.json.JSONObject
import java.lang.reflect.Field
import java.net.HttpURLConnection
import java.net.URL
import java.util.Scanner

/**
 * Created by Golemanovaa on 10.4.2016 г..
 */
class MyWidgedProvider : AppWidgetProvider() {

    private var temp: String? = null
    private var condition: String? = null
    private var iconId = 0

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        val pref: LocationPreference = LocationPreference.getInstance(context)
        city = "Sofia"
        country = ""
        countryCode = ""

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        if (netInfo != null && netInfo.isConnectedOrConnecting) {
            for (widgetId in appWidgetIds) {
                val remoteViews = RemoteViews(context.packageName, R.layout.widget_layout)

                val intent = Intent(context, WidgedService::class.java)

                intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

                // Fixed code (Add FLAG_IMMUTABLE)
                var flags = PendingIntent.FLAG_UPDATE_CURRENT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    flags = flags or PendingIntent.FLAG_IMMUTABLE
                }
                val pendingIntent = PendingIntent.getService(context, 0, intent, flags)

                // request the AppWidgetManager object to update the app widget
                remoteViews.setOnClickPendingIntent(R.id.widged_layout_btn_sync, pendingIntent)

                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        } else {
            // iterate through all of our widgets (in case the user has placed multiple widgets)
            for (widgetId in appWidgetIds) {
                val remoteViews = RemoteViews(
                    context.getPackageName(),
                    R.layout.widget_layout
                )
                if (!pref.hasNull()) {
                    remoteViews.setTextViewText(R.id.widget_layout_tv_city, city)
                    remoteViews.setTextViewText(R.id.widget_layout_tv_country, country)
                    this.condition = pref.condition
                    remoteViews.setTextViewText(R.id.widget_layout_tv_condition, this.condition)
                    this.temp = pref.temperature
                    remoteViews.setTextViewText(R.id.widged_layout_tv_degree, this.temp + "℃")

                    val field: Field?
                    try {
                        field = drawable::class.java.getDeclaredField(pref.icon)
                        iconId = field.getInt(this)
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }
                    remoteViews.setImageViewResource(R.id.widget_layout_iv_icon, iconId)
                } else {
                    remoteViews.setTextViewText(R.id.widget_layout_tv_condition, "No Internet Connection :(")
                }

                //update when the update button is clicked
                val intent = Intent(context, MyWidgedProvider::class.java)
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                //the widgets that should be updated (all of the app widgets)
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)

                // Fixed code (Add FLAG_IMMUTABLE)
                var flags = PendingIntent.FLAG_UPDATE_CURRENT
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    flags = flags or PendingIntent.FLAG_IMMUTABLE
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    0, intent, flags
                )

                // request the AppWidgetManager object to update the app widget
                remoteViews.setOnClickPendingIntent(R.id.widged_layout_btn_sync, pendingIntent)
                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
        }
    }

    fun setInfo(city: String?, country: String?, countryCode: String?) {
        Companion.city = city
        Companion.country = country
        Companion.countryCode = countryCode
    }

    class WidgedService : IntentService("WidgedService") {

        override fun onHandleIntent(intent: Intent?) {
            try {
                val url =
                    URL("https://api.weatherapi.com/v1" + "/current.json" + "?q=" + city + "&key=" + CurrentWeatherFragment.API_KEY)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val sc = Scanner(connection.getInputStream())
                val body = StringBuilder()
                while (sc.hasNextLine()) {
                    body.append(sc.nextLine())
                }
                val info = body.toString()

                val jsonData = JSONObject(info)
                val currentWeather = jsonData.get("current") as JSONObject
                val weatherCondition = currentWeather.get("condition") as JSONObject

                val condition = weatherCondition.getString("text")
                val currentTemp = currentWeather.getDouble("temp_c")
                val temp = Math.round(currentTemp).toString()


                val icon = weatherCondition.getInt("code")
                val isNight = weatherCondition.getString("icon").contains("night")

                val iconId = getImageResource(icon, isNight)

                val remoteV = RemoteViews(this.getPackageName(), R.layout.widget_layout)
                remoteV.setTextViewText(R.id.widget_layout_tv_city, city)
                remoteV.setTextViewText(R.id.widget_layout_tv_country, country)
                remoteV.setTextViewText(R.id.widget_layout_tv_condition, condition)
                remoteV.setTextViewText(R.id.widged_layout_tv_degree, temp + "℃")
                remoteV.setImageViewResource(R.id.widget_layout_iv_icon, iconId)


                val thisWidget = ComponentName(this, MyWidgedProvider::class.java)
                val appWidgetManager = AppWidgetManager.getInstance(this)

                appWidgetManager.updateAppWidget(thisWidget, remoteV)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun getImageResource(code: Int, isNight: Boolean) = when (code) {
            1000 -> if (isNight) drawable.sunny_night else drawable.sunny

            1003 -> if (isNight) drawable.partlycloudy_night else drawable.partlycloudy

            1006 -> if (isNight) drawable.mostlycloudy_night else drawable.mostlycloudy

            1009 -> if (isNight) drawable.cloudy_night else drawable.cloudy

            1030, 1135, 1147 -> if (isNight) drawable.fog_night else drawable.fog

            1063 -> if (isNight) drawable.chancerain_night else drawable.chancerain

            1072, 1066 -> if (isNight) drawable.chancesnow_night else drawable.chancesnow

            1069 -> if (isNight) drawable.chancesleet_night else drawable.chancesleet

            1273, 1276, 1279, 1282, 1087 -> if (isNight) drawable.tstorms_night else drawable.tstorms

            1114, 1210, 1213, 1216, 1219, 1222, 1225,
            1237, 1255, 1258, 1261, 1264, 1171, 1117 -> if (isNight) drawable.snow_night else drawable.snow

            1150, 1168, 1153 -> if (isNight) drawable.hazy_night else drawable.hazy

            1180, 1186, 1198, 1189, 1192, 1201,
            1240, 1243, 1246, 1183 -> if (isNight) drawable.rain_night else drawable.rain

            1207, 1249, 1252, 1204 -> if (isNight) drawable.sleet_night else drawable.sleet

            else -> drawable.icon_not_available
        }
    }

    companion object {
        private var city: String? = null
        private var country: String? = null
        private var countryCode: String? = null

        // This method is used to update the static location info from other parts of the app
        fun setInfo(city: String?, country: String?, countryCode: String?) {
            this.city = city
            this.country = country
            this.countryCode = countryCode
        }
    }
}
