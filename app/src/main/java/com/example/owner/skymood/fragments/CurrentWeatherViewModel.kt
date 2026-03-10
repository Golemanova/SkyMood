package com.example.owner.skymood.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.owner.skymood.data.NetworkModule
import com.example.owner.skymood.data.WeatherApiService
import com.example.owner.skymood.data.WeatherResponse
import kotlinx.coroutines.launch

class CurrentWeatherViewModel : ViewModel() {

    private val weatherApiService = NetworkModule.createService(WeatherApiService::class.java)

    private val _weatherData = MutableLiveData<WeatherResponse>()
    val weatherData: LiveData<WeatherResponse> = _weatherData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchWeather(city: String, apiKey: String) = viewModelScope.launch {
        _isLoading.value = true
        _error.value = null
        try {
            val response = weatherApiService.getCurrentWeather(city, apiKey)
            _weatherData.value = response
        } catch (e: Exception) {
            _error.value = "Failure: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }
}
