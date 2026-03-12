package com.example.owner.skymood.data.responses

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    @SerializedName("location") val location: Location,
    @SerializedName("current") val current: CurrentForecast,
    @SerializedName("forecast") val forecast: Forecast,
    @SerializedName("alerts") val alerts: Alerts
)

data class CurrentForecast(
    @SerializedName("last_updated_epoch") val lastUpdatedEpoch: Long,
    @SerializedName("last_updated") val lastUpdated: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    @SerializedName("is_day") val isDay: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("pressure_mb") val pressureMb: Double,
    @SerializedName("pressure_in") val pressureIn: Double,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipIn: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("cloud") val cloud: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    @SerializedName("windchill_c") val windChillC: Double,
    @SerializedName("windchill_f") val windChillF: Double,
    @SerializedName("heatindex_c") val heatIndexC: Double,
    @SerializedName("heatindex_f") val heatIndexF: Double,
    @SerializedName("dewpoint_c") val dewPointC: Double,
    @SerializedName("dewpoint_f") val dewPointF: Double,
    @SerializedName("vis_km") val visKm: Double,
    @SerializedName("vis_miles") val visMiles: Double,
    @SerializedName("uv") val uv: Double,
    @SerializedName("gust_mph") val gustMph: Double,
    @SerializedName("gust_kph") val gustKph: Double,
    @SerializedName("air_quality") val airQuality: AirQuality?
)

data class AirQuality(
    @SerializedName("co") val co: Double,
    @SerializedName("no2") val no2: Double,
    @SerializedName("o3") val o3: Double,
    @SerializedName("so2") val so2: Double,
    @SerializedName("pm2_5") val pm25: Double,
    @SerializedName("pm10") val pm10: Double,
    @SerializedName("us-epa-index") val usEpaIndex: Int,
    @SerializedName("gb-defra-index") val gbDefraIndex: Int
)

data class Forecast(
    @SerializedName("forecastday") val forecastDay: List<ForecastDay>
)

data class ForecastDay(
    @SerializedName("date") val date: String,
    @SerializedName("date_epoch") val dateEpoch: Long,
    @SerializedName("day") val day: Day,
    @SerializedName("astro") val astro: Astro,
    @SerializedName("hour") val hour: List<Hour>
)

data class Day(
    @SerializedName("maxtemp_c") val maxTempC: Double,
    @SerializedName("maxtemp_f") val maxTempF: Double,
    @SerializedName("mintemp_c") val minTempC: Double,
    @SerializedName("mintemp_f") val minTempF: Double,
    @SerializedName("avgtemp_c") val avgTempC: Double,
    @SerializedName("avgtemp_f") val avgTempF: Double,
    @SerializedName("maxwind_mph") val maxWindMph: Double,
    @SerializedName("maxwind_kph") val maxWindKph: Double,
    @SerializedName("totalprecip_mm") val totalPrecipMm: Double,
    @SerializedName("totalprecip_in") val totalPrecipIn: Double,
    @SerializedName("totalsnow_cm") val totalSnowCm: Double,
    @SerializedName("avgvis_km") val avgVisKm: Double,
    @SerializedName("avgvis_miles") val avgVisMiles: Double,
    @SerializedName("avghumidity") val avgHumidity: Double,
    @SerializedName("daily_will_it_rain") val dailyWillItRain: Int,
    @SerializedName("daily_chance_of_rain") val dailyChanceOfRain: Int,
    @SerializedName("daily_will_it_snow") val dailyWillItSnow: Int,
    @SerializedName("daily_chance_of_snow") val dailyChanceOfSnow: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("uv") val uv: Double
)

data class Astro(
    @SerializedName("sunrise") val sunrise: String,
    @SerializedName("sunset") val sunset: String,
    @SerializedName("moonrise") val moonrise: String,
    @SerializedName("moonset") val moonset: String,
    @SerializedName("moon_phase") val moonPhase: String,
    @SerializedName("moon_illumination") val moonIllumination: Int,
    @SerializedName("is_moon_up") val isMoonUp: Int,
    @SerializedName("is_sun_up") val isSunUp: Int
)

data class Hour(
    @SerializedName("time_epoch") val timeEpoch: Long,
    @SerializedName("time") val time: String,
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("temp_f") val tempF: Double,
    @SerializedName("is_day") val isDay: Int,
    @SerializedName("condition") val condition: Condition,
    @SerializedName("wind_mph") val windMph: Double,
    @SerializedName("wind_kph") val windKph: Double,
    @SerializedName("wind_degree") val windDegree: Int,
    @SerializedName("wind_dir") val windDir: String,
    @SerializedName("pressure_mb") val pressureMb: Double,
    @SerializedName("pressure_in") val pressureIn: Double,
    @SerializedName("precip_mm") val precipMm: Double,
    @SerializedName("precip_in") val precipIn: Double,
    @SerializedName("snow_cm") val snowCm: Double,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("cloud") val cloud: Int,
    @SerializedName("feelslike_c") val feelsLikeC: Double,
    @SerializedName("feelslike_f") val feelsLikeF: Double,
    @SerializedName("windchill_c") val windChillC: Double,
    @SerializedName("windchill_f") val windChillF: Double,
    @SerializedName("heatindex_c") val heatIndexC: Double,
    @SerializedName("heatindex_f") val heatIndexF: Double,
    @SerializedName("dewpoint_c") val dewPointC: Double,
    @SerializedName("dewpoint_f") val dewPointF: Double,
    @SerializedName("will_it_rain") val willItRain: Int,
    @SerializedName("chance_of_rain") val chanceOfRain: Int,
    @SerializedName("will_it_snow") val willItSnow: Int,
    @SerializedName("chance_of_snow") val chanceOfSnow: Int,
    @SerializedName("vis_km") val visKm: Double,
    @SerializedName("vis_miles") val visMiles: Double,
    @SerializedName("gust_mph") val gustMph: Double,
    @SerializedName("gust_kph") val gustKph: Double,
    @SerializedName("uv") val uv: Double
)

data class Alerts(
    @SerializedName("alert") val alert: List<Alert>
)

data class Alert(
    @SerializedName("headline") val headline: String?,
    @SerializedName("msgtype") val msgType: String?,
    @SerializedName("severity") val severity: String?,
    @SerializedName("urgency") val urgency: String?,
    @SerializedName("areas") val areas: String?,
    @SerializedName("category") val category: String?,
    @SerializedName("certainty") val certainty: String?,
    @SerializedName("event") val event: String?,
    @SerializedName("note") val note: String?,
    @SerializedName("effective") val effective: String?,
    @SerializedName("expires") val expires: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("instruction") val instruction: String?
)
