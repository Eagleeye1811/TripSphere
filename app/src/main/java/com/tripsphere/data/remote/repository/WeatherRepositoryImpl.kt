package com.tripsphere.data.remote.repository

import com.tripsphere.data.remote.api.WeatherApiService
import com.tripsphere.domain.model.Weather
import com.tripsphere.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val apiService: WeatherApiService
) : WeatherRepository {

    override suspend fun getWeather(latitude: Double, longitude: Double): Result<Weather> {
        return try {
            val response = apiService.getCurrentWeather(latitude, longitude)
            val cw = response.currentWeather
            Result.success(
                Weather(
                    temperature = cw.temperature,
                    windspeed = cw.windspeed,
                    weatherCode = cw.weatherCode,
                    isDay = cw.isDay == 1,
                    description = mapWeatherCode(cw.weatherCode),
                    icon = mapWeatherIcon(cw.weatherCode, cw.isDay == 1)
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapWeatherCode(code: Int): String = when (code) {
        0 -> "Clear Sky"
        1, 2, 3 -> "Partly Cloudy"
        45, 48 -> "Foggy"
        51, 53, 55 -> "Drizzle"
        61, 63, 65 -> "Rain"
        71, 73, 75 -> "Snow"
        80, 81, 82 -> "Rain Showers"
        85, 86 -> "Snow Showers"
        95 -> "Thunderstorm"
        96, 99 -> "Thunderstorm with Hail"
        else -> "Variable"
    }

    private fun mapWeatherIcon(code: Int, isDay: Boolean): String = when (code) {
        0 -> if (isDay) "☀️" else "🌙"
        1, 2, 3 -> if (isDay) "⛅" else "🌤️"
        45, 48 -> "🌫️"
        51, 53, 55 -> "🌦️"
        61, 63, 65 -> "🌧️"
        71, 73, 75 -> "❄️"
        80, 81, 82 -> "🌦️"
        85, 86 -> "🌨️"
        95, 96, 99 -> "⛈️"
        else -> "🌡️"
    }
}
