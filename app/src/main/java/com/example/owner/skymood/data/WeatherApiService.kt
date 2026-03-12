package com.example.owner.skymood.data

import com.example.owner.skymood.data.responses.ForecastResponse
import com.example.owner.skymood.data.responses.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("key") apiKey: String = API_KEY,
    ): WeatherResponse

    @GET("v1/forecast.json")
    suspend fun getForecast(
        @Query("q") location: String,
        @Query("days") days: Int = 1,
        @Query("aqi") aqi: String = "yes",
        @Query("alerts") alerts: String = "yes",
        @Query("key") apiKey: String = API_KEY,
    ): ForecastResponse
}

private const val API_KEY: String = "5229b753f41a4812b74165454260402"
