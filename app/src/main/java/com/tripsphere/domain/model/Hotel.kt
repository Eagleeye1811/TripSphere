package com.tripsphere.domain.model

data class Hotel(
    val id: String,
    val name: String,
    val type: String,               // hotel, hostel, guest_house, motel
    val stars: Int?,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val distanceMeters: Int,
    val phone: String?,
    val website: String?,
    val estimatedPriceRange: String = "",
    val imageUrl: String = ""
)
