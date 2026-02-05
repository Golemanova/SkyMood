package com.example.owner.skymood.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.owner.skymood.R
import com.example.owner.skymood.model.WeeklyWeather

/**
 * Created by owner on 05/04/2016.
 */
class WeeklyAdapter(
    private val weathers: ArrayList<WeeklyWeather>, private val context: Context?
) : RecyclerView.Adapter<HourlyWeekViewHolder?>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeekViewHolder {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.row_week, parent, false)
        return HourlyWeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: HourlyWeekViewHolder, position: Int) {
        val weather = weathers[position]
        holder.icon!!.setImageBitmap(weather.icon)
        holder.temp!!.text = weather.max + " / " + weather.min + "°"
        holder.condition!!.text = weather.condition
        holder.hour!!.text = weather.day
    }

    override fun getItemCount() = weathers.size
}
