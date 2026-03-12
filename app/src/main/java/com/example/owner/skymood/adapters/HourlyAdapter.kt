package com.example.owner.skymood.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.owner.skymood.databinding.RowHourBinding
import com.example.owner.skymood.model.HourlyWeather

/**
 * Created by owner on 04/04/2016.
 */
class HourlyAdapter(
    private val context: Context?, 
    private val weathers: ArrayList<HourlyWeather>
) : RecyclerView.Adapter<HourlyAdapter.ViewHolder>() {

    class ViewHolder(val binding: RowHourBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowHourBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val weather = weathers[position]
        with(holder.binding) {
            rowHourTvHour.text = weather.hour
            rowHourTvTemp.text = "${weather.temp}℃"
            weather.icon?.let { rowHourIvIcon.setImageResource(it) }

        }
    }

    override fun getItemCount(): Int {
        if (weathers.size > 24) return 24
        return weathers.size
    }
}
