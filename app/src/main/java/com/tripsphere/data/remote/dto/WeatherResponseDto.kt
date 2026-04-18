package com.tripsphere.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("current_weather") val currentWeather: CurrentWeatherDto = CurrentWeatherDto()
)

data class CurrentWeatherDto(
    @SerializedName("temperature") val temperature: Double = 0.0,
    @SerializedName("windspeed") val windspeed: Double = 0.0,
    @SerializedName("weathercode") val weatherCode: Int = 0,
    @SerializedName("is_day") val isDay: Int = 1
)
