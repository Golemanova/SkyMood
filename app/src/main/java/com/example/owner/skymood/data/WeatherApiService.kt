package com.example.owner.skymood.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/current.json")
    suspend fun getCurrentWeather(
        @Query("q") location: String,
        @Query("key") apiKey: String,
    ): WeatherResponse
}
