package com.example.owner.skymood.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.owner.skymood.R
import com.example.owner.skymood.adapters.HourlyAdapter
import com.example.owner.skymood.adapters.WeeklyAdapter
import com.example.owner.skymood.model.HourlyWeather
import com.example.owner.skymood.model.WeeklyWeather

class HourlyWeatherFragment : Fragment() {

    var hourlyWeatherArray: ArrayList<HourlyWeather> = arrayListOf()

    var weeklyWeatherArray: ArrayList<WeeklyWeather> = arrayListOf()

    private  var hourlyRecycler: RecyclerView? = null
    private  var weeklyRecycler: RecyclerView? = null
    lateinit var layout: LinearLayout
    lateinit var text: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_hourly_weather, container, false)
        this.setRetainInstance(true)

        // hourly recycler
        hourlyRecycler =
            view.findViewById<View?>(R.id.fragment_hourly_weather_rv_recycler_hourly) as RecyclerView
        hourlyRecycler?.setLayoutManager(
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        )
        val adapter = HourlyAdapter(context, this.hourlyWeatherArray)
        hourlyRecycler?.setAdapter(adapter)

        //weekly recycler
        weeklyRecycler =
            view.findViewById<View?>(R.id.fragment_hourly_weather_rv_recycler_weekly) as RecyclerView
        weeklyRecycler?.setLayoutManager(LinearLayoutManager(context))
        val weeklyAdapter = WeeklyAdapter(this.weeklyWeatherArray, context)
        weeklyRecycler?.setAdapter(weeklyAdapter)

        return view
    }

    val adapter: HourlyAdapter? = hourlyRecycler?.adapter as HourlyAdapter?

    val weekAdapter: WeeklyAdapter? = weeklyRecycler?.adapter as WeeklyAdapter?
}
