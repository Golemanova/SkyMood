package com.example.owner.skymood.data

import com.example.owner.skymood.data.responses.ForecastResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

/**
 * Repository that handles weather data fetching and caching.
 *
 */
object WeatherRepository {

    // The API service to fetch weather data from.
    private val apiService = NetworkModule.createService(WeatherApiService::class.java)

    // In-memory cache for forecast responses, keyed by location string.
    private val _forecastCache = MutableStateFlow<Map<String, ForecastResponse>>(emptyMap())

    private var lastLocation: String = ""


    /**
     * Returns a [Flow] of [ForecastResponse] for the specified [location].
     *
     * It will emit the cached value first if available and [skipCache] is false.
     * If no cached value is present or [skipCache] is true, it fetches a new forecast from the API
     * and updates the cache, notifying all active listeners for this location.
     *
     * @param location The location to get the forecast for (e.g., "London").
     * @param skipCache If true, forces a network refresh even if a value is already cached.
     * @return A flow that emits the forecast and subsequent updates.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    fun getForecast(location: String, skipCache: Boolean = false): Flow<ForecastResponse> {
        return flow {
            lastLocation = location
            val currentCache = _forecastCache.value[location]
            if (currentCache == null || skipCache) {
                val networkResponse = apiService.getForecast(location)
                _forecastCache.update { it + (location to networkResponse) }
            }
            emit(Unit)
        }.flatMapLatest {
            // Observe changes to the cache for this specific location
            _forecastCache
                .mapNotNull { it[location] }
        }.distinctUntilChanged()
    }

    fun getLastLocationForecast(): Flow<ForecastResponse>{
        return getForecast(lastLocation)
    }
}
