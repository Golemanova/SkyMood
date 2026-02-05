package com.example.owner.skymood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.owner.skymood.R
import com.example.owner.skymood.model.HourlyWeather

/**
 * Created by owner on 04/04/2016.
 */
class HourlyAdapter(
    private val context: Context?, private val weathers: ArrayList<HourlyWeather>
) : RecyclerView.Adapter<HourlyWeekViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeekViewHolder {
        val inflater = LayoutInflater.from(context)
        //TODO wtf?! see this layouts row_hour & row_week
        val row = inflater.inflate(R.layout.row_hour, parent, false)
        return HourlyWeekViewHolder(row)
    }

    override fun onBindViewHolder(holder: HourlyWeekViewHolder, position: Int) {
        val weather = weathers[position]
        holder.hour.text = weather.hour + ":00"
        holder.temp.text = weather.temp + " ℃"
        holder.icon.setImageBitmap(weather.icon)
    }

    override fun getItemCount(): Int {
        if (weathers.size > 24) return 24
        return weathers.size
    }
}
