package com.tripsphere.domain.model

data class Weather(
    val temperature: Double,
    val windspeed: Double,
    val weatherCode: Int,
    val isDay: Boolean,
    val description: String,
    val icon: String
)
