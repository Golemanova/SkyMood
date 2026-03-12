package com.example.owner.skymood.fragments.hourly

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owner.skymood.data.WeatherRepository
import com.example.owner.skymood.data.responses.ForecastResponse
import com.example.owner.skymood.fragments.current.getImageResource
import com.example.owner.skymood.model.HourlyWeather
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class HourlyWeatherViewModel : ViewModel() {

    private val _hourlyWeather = MutableStateFlow<List<HourlyWeather>>(emptyList())
    val hourlyWeather: StateFlow<List<HourlyWeather>> = _hourlyWeather.asStateFlow()

    init {
        fetchHourlyWeather()
    }

    private fun fetchHourlyWeather() {
        viewModelScope.launch {
            WeatherRepository.getLastLocationForecast()
                .collect { response ->
                    _hourlyWeather.value = mapToHourlyWeather(response)
                }
        }
    }

    private fun mapToHourlyWeather(response: ForecastResponse): List<HourlyWeather> {
        val hourlyList = mutableListOf<HourlyWeather>()
        response.forecast.forecastDay.forEach { forecastDay ->
            forecastDay.hour.forEach { hourData ->
                hourlyList.add(
                    HourlyWeather(
                        hour = hourData.time.split(" ")[1],
                        condition = hourData.condition.text,
                        temp = hourData.tempC.toInt().toString(),
                        icon = hourData.condition.code.getImageResource(hourData.isDay == 0)
                    )
                )
            }
        }
        return hourlyList
    }
}
