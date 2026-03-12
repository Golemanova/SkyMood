package com.example.owner.skymood.fragments.hourly

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.owner.skymood.adapters.HourlyAdapter
import com.example.owner.skymood.adapters.WeeklyAdapter
import com.example.owner.skymood.databinding.FragmentHourlyWeatherBinding
import com.example.owner.skymood.model.HourlyWeather
import com.example.owner.skymood.model.WeeklyWeather
import kotlinx.coroutines.launch
import java.util.Calendar

class HourlyWeatherFragment : Fragment() {

    private lateinit var binding: FragmentHourlyWeatherBinding

    private val viewModel: HourlyWeatherViewModel by viewModels()

    var hourlyWeatherArray: ArrayList<HourlyWeather> = arrayListOf()
    var weeklyWeatherArray: ArrayList<WeeklyWeather> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHourlyWeatherBinding.inflate(inflater, container, false)

        // hourly recycler
        binding.fragmentHourlyWeatherRvRecyclerHourly.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = HourlyAdapter(context, this.hourlyWeatherArray)
        binding.fragmentHourlyWeatherRvRecyclerHourly.adapter = adapter

        //weekly recycler
        binding.fragmentHourlyWeatherRvRecyclerWeekly.layoutManager = LinearLayoutManager(context)
        val weeklyAdapter = WeeklyAdapter(this.weeklyWeatherArray, context)
        binding.fragmentHourlyWeatherRvRecyclerWeekly.adapter = weeklyAdapter

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.hourlyWeather.collect { hourlyList ->
                    if (hourlyList.isNotEmpty()) {
                        hourlyWeatherArray.clear()
                        hourlyWeatherArray.addAll(hourlyList)
                        adapter?.notifyDataSetChanged()

                        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                        if (currentHour < hourlyWeatherArray.size) {
                            binding.fragmentHourlyWeatherRvRecyclerHourly.scrollToPosition(currentHour)
                        }
                    }
                }
            }
        }
    }

    val adapter: HourlyAdapter? get() = binding.fragmentHourlyWeatherRvRecyclerHourly.adapter as? HourlyAdapter

    val weekAdapter: WeeklyAdapter? get() = binding.fragmentHourlyWeatherRvRecyclerWeekly.adapter as? WeeklyAdapter
}
