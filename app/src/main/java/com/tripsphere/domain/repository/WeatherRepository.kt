package com.tripsphere.domain.repository

import com.tripsphere.domain.model.Weather

interface WeatherRepository {
    suspend fun getWeather(latitude: Double, longitude: Double): Result<Weather>
}
