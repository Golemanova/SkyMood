package com.example.owner.skymood.fragments.current

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owner.skymood.data.WeatherRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class CurrentWeatherViewModel : ViewModel() {

    private val _weatherData = MutableLiveData<CurrentWeatherData>()
    val weatherData: LiveData<CurrentWeatherData> = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchWeather(city: String, skipCache: Boolean = false) = viewModelScope.launch {
        WeatherRepository.getForecast(city, skipCache)
            .onStart { _isLoading.value = true }
            .catch { e ->
                _error.value = "Failure: ${e.message}"
                _isLoading.value = false
            }
            .collect { response ->
                _weatherData.value = response.toCurrentWeatherData()
                _isLoading.value = false
            }
    }
}